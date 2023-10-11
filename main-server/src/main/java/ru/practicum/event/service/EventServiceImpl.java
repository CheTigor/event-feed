package ru.practicum.event.service;

import com.querydsl.core.BooleanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.StatisticClient;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.repository.CommentRepository;
import ru.practicum.constants.Constants;
import ru.practicum.dto.HitRequest;
import ru.practicum.dto.ViewStatsResponse;
import ru.practicum.event.dto.*;
import ru.practicum.event.enums.EventState;
import ru.practicum.event.enums.EventStateAdminAction;
import ru.practicum.event.enums.Sorts;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.QEvent;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.DateValidationException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.UnavailablePublishedEventException;
import ru.practicum.exception.ValidationException;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class EventServiceImpl implements EventService {

    private final StatisticClient client;
    private final EventRepository eventRep;
    private final CategoryRepository catRep;
    private final UserRepository userRep;
    private final CommentRepository comRep;

    @Autowired
    public EventServiceImpl(StatisticClient client, EventRepository eventRep, CategoryRepository catRep,
                            UserRepository userRep, CommentRepository comRep) {
        this.client = client;
        this.eventRep = eventRep;
        this.catRep = catRep;
        this.userRep = userRep;
        this.comRep = comRep;
    }

    @Override
    public List<EventShortResponseDto> getEventsByUserId(Long userId, Integer from, Integer size,
                                                         HttpServletRequest request) {
        final List<EventShortResponseDto> response = eventRep.findByInitiator_Id(userId, PageRequest.of(from / size, size))
                .stream().map(EventMapper::toEventShort).collect(Collectors.toList());
        saveHit(request);
        return response;
    }

    @Override
    @Transactional
    public EventResponseDto createEvent(Long userId, EventRequestDto request) {
        final LocalDateTime eventDate = LocalDateTime.parse(request.getEventDate(),
                DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT));
        dateValidation(eventDate);
        final Category category = catRep.findById(request.getCategory()).orElseThrow(() -> new NotFoundException(
                String.format("В базе данных нет категории с id - %s", request.getCategory())));
        final User user = userRep.findById(userId).orElseThrow(() -> new NotFoundException(
                String.format("В базе данных нет пользователя с id - %s", userId)));
        final Event event = eventRep.save(new Event(null, request.getAnnotation(), category, 0,
                LocalDateTime.now().withNano(0), request.getDescription(), eventDate, user, request.getLocation().getLat(),
                request.getLocation().getLon(), request.getPaid(), request.getParticipantLimit(), null,
                request.getRequestModeration(), EventState.PENDING, request.getTitle(), 0L));
        final EventResponseDto finalEvent = EventMapper.toEventResponse(event, findComments(event.getId()));
        log.debug("В базе данных сохранен event: {}", finalEvent);
        return finalEvent;
    }

    @Override
    public EventResponseDto getEventById(Long userId, Long eventId, HttpServletRequest request) {
        final Event event = eventRep.findById(eventId).orElseThrow(() -> new NotFoundException(
                String.format("В базе данных нет события с id - %s", eventId)));
        if (!event.getInitiator().getId().equals(userId)) {
            throw new NotFoundException(String.format(
                    "User с id: %d не является инициатором события, полный просмотр недоступен", eventId));
        }
        saveHit(request);
        event.setViews(getViews(request));
        final EventResponseDto response = EventMapper.toEventResponse(eventRep.save(event), findComments(eventId));
        log.debug("В БД сохранено новое значение views для event: {}", response);
        return response;
    }

    @Override
    @Transactional
    public EventResponseDto updateEventByUser(UpdateEventUserRequestDto updEvent, Long userId, Long eventId) {
        final Event event = eventRep.findById(eventId).orElseThrow(() -> new NotFoundException(
                String.format("В базе данных нет события с id - %s", eventId)));
        if (!event.getInitiator().getId().equals(userId)) {
            throw new NotFoundException(String.format(
                    "User с id: %d не является создателем события, полный просмотр недоступен", eventId));
        }
        if (event.getState() == EventState.PUBLISHED) {
            throw new UnavailablePublishedEventException("Невозможно обновить опубликованное или закрытое событие");
        }
        if (updEvent.getCategory() != null) {
            event.setCategory(updEvent.getCategory());
        }
        if (updEvent.getStateAction() != null) {
            switch (updEvent.getStateAction()) {
                case SEND_TO_REVIEW:
                    event.setState(EventState.PENDING);
                    break;
                case CANCEL_REVIEW:
                    event.setState(EventState.CANCELED);
                    break;
                default:
                    break;
            }
        }
        final EventResponseDto finalEvent = EventMapper.toEventResponse(eventRep.save(
                updateEventParams(event, EventMapper.toCommonUpdateParams(updEvent))), findComments(eventId));
        log.debug("В базе данных сохранен event: {}", finalEvent);
        return finalEvent;
    }

    @Override
    public List<EventResponseDto> getAdminEventsByParams(EventAdminParams params, Integer from, Integer size) {
        final QEvent EVENT = QEvent.event;
        BooleanBuilder builder = new BooleanBuilder(EVENT.isNotNull());
        if (params.getUsers() != null) {
            if (!params.getUsers().isEmpty()) {
                if (params.getUsers().get(0) == 0) {
                    throw new ValidationException(String.format("Минимальное значение для userId = 1, а передано: %d",
                            params.getUsers().get(0)));
                } else {
                    builder.and(EVENT.initiator.id.in(params.getUsers()));
                }
            }
        }
        if (params.getStates() != null && !params.getStates().isEmpty()) {
            builder.and(EVENT.state.in(params.getStates()));
        }
        if (params.getCategories() != null) {
            if (!params.getCategories().isEmpty()) {
                if (params.getCategories().get(0) == 0) {
                    throw new ValidationException(String.format("Минимальное значение для categoryId = 1, а передано: %d",
                            params.getCategories().get(0)));
                } else {
                    builder.and(EVENT.category.id.in(params.getCategories()));
                }
            }
        }
        if (params.getRangeStart() != null && params.getRangeEnd() != null) {
            if (params.getRangeStart().isAfter(params.getRangeEnd())) {
                throw new DateValidationException("rangeEnd не может быть раньше rangeStart");
            }
            builder.and(EVENT.eventDate.between(params.getRangeStart(), params.getRangeEnd()));
        } else if (params.getRangeStart() != null) {
            builder.and(EVENT.eventDate.after(params.getRangeStart()));
        } else if (params.getRangeEnd() != null) {
            builder.and(EVENT.eventDate.before(params.getRangeEnd()));
        }
        return eventRep.findAll(builder, PageRequest.of(
                        from / size, size)).stream().map(ev -> EventMapper.toEventResponse(ev,
                        findComments(ev.getId()))).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventResponseDto updateEventByAdmin(UpdateEventAdminRequestDto updEvent, Long eventId) {
        final Event event = eventRep.findById(eventId).orElseThrow(() -> new NotFoundException(String.format(
                "В базе данных нет события с id - %s", eventId)));
        if (event.getState() == EventState.PUBLISHED || event.getState() == EventState.CANCELED) {
            throw new UnavailablePublishedEventException(String.format("Событие со статусом - %s не может быть изменено",
                    event.getState()));
        }
        if (updEvent.getEventDate() != null) {
            final LocalDateTime dateTime = parseDateTime(updEvent.getEventDate());
            if (dateTime.isBefore(LocalDateTime.now().withNano(0).plusHours(1))) {
                throw new DateValidationException(
                        String.format("Дата начала события должно быть позже на 1 час публикации события, today: %s, eventDate: %s",
                                LocalDateTime.now(), dateTime));
            }
        }
        if (updEvent.getStateAction() != null) {
            if (updEvent.getStateAction().equals(EventStateAdminAction.PUBLISH_EVENT)) {
                event.setState(EventState.PUBLISHED);
            } else if (updEvent.getStateAction().equals(EventStateAdminAction.REJECT_EVENT)) {
                event.setState(EventState.CANCELED);
            }
        }
        final EventResponseDto finalEvent = EventMapper.toEventResponse(eventRep.save(
                updateEventParams(event, EventMapper.toCommonUpdateParams(updEvent))), findComments(eventId));
        log.debug("В БД сохранен event: {}", finalEvent);
        return finalEvent;
    }

    @Override
    public List<EventShortResponseDto> getPublicEventsByParams(EventPublicParams params, Integer from, Integer size,
                                                               HttpServletRequest request) {
        final QEvent EVENT = QEvent.event;
        BooleanBuilder builder = new BooleanBuilder(EVENT.isNotNull());
        if (params.getText() != null && !params.getText().isBlank()) {
            builder.and(EVENT.annotation.containsIgnoreCase(params.getText())
                    .or(EVENT.description.containsIgnoreCase(params.getText())));
        }
        if (params.getCategories() != null && !params.getCategories().isEmpty() && params.getCategories().get(0) != 0) {
            builder.and(EVENT.category.id.in(params.getCategories()));
        }
        if (params.getPaid() != null) {
            builder.and(EVENT.paid.eq(params.getPaid()));
        }
        if (params.getRangeStart() == null && params.getRangeEnd() == null) {
            builder.and(EVENT.eventDate.after(LocalDateTime.now()));
        } else if (params.getRangeStart() != null && params.getRangeEnd() != null) {
            if (params.getRangeStart().isAfter(params.getRangeEnd())) {
                throw new DateValidationException("rangeEnd не может быть раньше rangeStart");
            }
            builder.and(EVENT.eventDate.between(params.getRangeStart(), params.getRangeEnd()));
        } else if (params.getRangeEnd() != null) {
            builder.and(EVENT.eventDate.before(params.getRangeEnd()));
        } else {
            builder.and(EVENT.eventDate.after(params.getRangeStart()));
        }
        if (params.getOnlyAvailable() != null) {
            if (params.getOnlyAvailable()) {
                builder.and(EVENT.participantLimit.eq(0).or(EVENT.participantLimit.goe(EVENT.confirmedRequests)));
            }
        }
        builder.and(EVENT.state.eq(EventState.PUBLISHED));
        if (params.getSort() != null) {
            if (params.getSort() == Sorts.EVENT_DATE) {
                final List<EventShortResponseDto> response = eventRep.findAll(builder,
                                PageRequest.of(from / size, size, Sort.Direction.ASC, "eventDate"))
                        .stream().map(EventMapper::toEventShort).collect(Collectors.toList());
                saveHit(request);
                return response;
            } else if (params.getSort() == Sorts.VIEWS) {
                final List<EventShortResponseDto> response = eventRep.findAll(builder,
                                PageRequest.of(from / size, size, Sort.Direction.DESC, "views"))
                        .stream().map(EventMapper::toEventShort).collect(Collectors.toList());
                saveHit(request);
                return response;
            }
        }
        final List<EventShortResponseDto> response = eventRep.findAll(builder, PageRequest.of(from / size, size)).stream().map(EventMapper::toEventShort)
                .collect(Collectors.toList());
        saveHit(request);
        return response;
    }

    @Override
    public EventResponseDto getPublicEventById(Long id, HttpServletRequest request) {
        final Event event = eventRep.findById(id).orElseThrow(() -> new NotFoundException(
                String.format("В базе данных нет события с id - %s", id)));
        if (event.getState() != EventState.PUBLISHED) {
            throw new NotFoundException(String.format("Событие не найдено или недоступно, eventId: %s", id));
        }
        saveHit(request);
        event.setViews(getViews(request));
        final EventResponseDto response = EventMapper.toEventResponse(eventRep.save(event), findComments(id));
        log.debug("В БД сохранено новое значение views для event: {}", response);
        return response;
    }

    private void saveHit(HttpServletRequest request) {
        client.saveHit(new HitRequest(Constants.MAIN_SERVICE_NAME, request.getRequestURI(), request.getRemoteAddr(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT))));
    }

    private void dateValidation(LocalDateTime date) {
        if (date.isBefore(LocalDateTime.now().withNano(0).plusHours(2))) {
            throw new DateValidationException(
                    String.format("Дата начала события должно быть позже на 2 часа создания события, today: %s, eventDate: %s",
                            LocalDateTime.now(), date));
        }
    }

    private LocalDateTime parseDateTime(String date) {
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT));
    }

    private Event updateEventParams(Event event, CommonUpdateParams updEvent) {
        if (updEvent.getAnnotation() != null) {
            event.setAnnotation(updEvent.getAnnotation());
        }
        if (updEvent.getDescription() != null) {
            event.setDescription(updEvent.getDescription());
        }
        if (updEvent.getEventDate() != null) {
            final LocalDateTime date = LocalDateTime.parse(updEvent.getEventDate(),
                    DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT));
            dateValidation(date);
            event.setEventDate(date);
        }
        if (updEvent.getLocation() != null) {
            if (updEvent.getLocation().getLat() != null) {
                event.setLocationLat(updEvent.getLocation().getLat());
            }
            if (updEvent.getLocation().getLon() != null) {
                event.setLocationLon(updEvent.getLocation().getLon());
            }
        }
        if (updEvent.getPaid() != null) {
            event.setPaid(updEvent.getPaid());
        }
        if (updEvent.getParticipantLimit() != null) {
            event.setParticipantLimit(updEvent.getParticipantLimit());
        }
        if (updEvent.getRequestModeration() != null) {
            event.setRequestModeration(updEvent.getRequestModeration());
        }
        if (updEvent.getTitle() != null) {
            event.setTitle(updEvent.getTitle());
        }
        return event;
    }

    private Long getViews(HttpServletRequest request) {
        List<ViewStatsResponse> stats = client.getStats(Constants.START_TIME, Constants.END_TIME, request.getRequestURI(),
                true).getBody();
        Long views = 0L;
        if (stats != null) {
            views = stats.stream().filter(e -> e.getUri().equals(request.getRequestURI())).findFirst().get().getHits();
        }
        return views;
    }

    private List<Comment> findComments(Long eventId) {
        List<Comment> comments = comRep.findByEventIdAndItsResponse(eventId, false, PageRequest.of(0, 10))
                .toList();
        return comments;
    }

}
