package ru.practicum.request.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.event.enums.EventState;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationResponseDto;
import ru.practicum.request.enums.RequestStatus;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.model.Request;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRep;
    private final EventRepository eventRep;
    private final UserRepository userRep;

    @Autowired
    public RequestServiceImpl(RequestRepository requestRep, EventRepository eventRep, UserRepository userRep) {
        this.requestRep = requestRep;
        this.eventRep = eventRep;
        this.userRep = userRep;
    }

    @Override
    public List<ParticipationResponseDto> getUserRequests(Long userId) {
        if (userRep.existsById(userId)) {
            return requestRep.findByRequester_Id(userId).stream().map(RequestMapper::toParticipationResponseDto)
                    .collect(Collectors.toList());
        } else {
            throw new NotFoundException(String.format("В базе данных нет пользователя с id - %s", userId));
        }
    }

    @Override
    @Transactional
    public ParticipationResponseDto createRequest(Long userId, Long eventId) {
        final User user = userRep.findById(userId).orElseThrow(() -> new NotFoundException(
                String.format("В базе данных нет пользователя с id - %s", userId)));
        final Event event = eventRep.findById(eventId).orElseThrow(() -> new NotFoundException(
                String.format("В базе данных нет события с id - %s", eventId)));
        if (event.getInitiator().equals(user)) {
            throw new DataIntegrityViolationException(String.format("Заявка на участие не может быть создана - " +
                    "пользователь с id: %d является создателем события с id: %d", userId, eventId));
        }
        if (event.getState() != EventState.PUBLISHED) {
            throw new DataIntegrityViolationException(String.format("Заявка на участие не может быть создана - " +
                    "события с id: %d еще не опубликовано", eventId));
        }
        if (requestRep.findByRequester_IdAndEvent_Id(userId, eventId) != null) {
            throw new DataIntegrityViolationException(String.format("Заявка на участие не может быть создана - " +
                    "пользователь с id: %d уже создал заявку на участие в событии с id: %d", userId, eventId));
        }
        if (event.getParticipantLimit() == 0) {
            return buildRequest(event, user);
        } else if (event.getConfirmedRequests() < event.getParticipantLimit()) {
            return buildRequest(event, user);
        } else {
            throw new DataIntegrityViolationException(String.format("Заявка на участие не может быть создана - " +
                    "событие с id: %d достигло лимит участников в %d пользователей", eventId, event.getParticipantLimit()));
        }
    }

    @Override
    @Transactional
    public ParticipationResponseDto cancelRequestByUser(Long userId, Long requestId) {
        if (!userRep.existsById(userId)) {
            throw new NotFoundException(String.format("В базе данных нет пользователя с id - %s", userId));
        }
        final Request request = requestRep.findByIdAndRequester_Id(requestId, userId).orElseThrow(() -> new NotFoundException(
                String.format("В базе данных нет заявки на участие в событии с id - %d и пользователем с id - %d",
                        requestId, userId)));
        if (request.getStatus() == RequestStatus.CONFIRMED) {
            request.getEvent().setConfirmedRequests(request.getEvent().getConfirmedRequests() - 1);
            final Event event = eventRep.save(request.getEvent());
            log.debug("В БД обновлено поле confirmedRequests в event: {}", event);
        }
        request.setStatus(RequestStatus.CANCELED);
        final Request finalRequest = requestRep.save(request);
        log.debug("В БД обновлена заявка на участие (отмена участия): {}", finalRequest);
        return RequestMapper.toParticipationResponseDto(finalRequest);
    }

    @Override
    public List<ParticipationResponseDto> getEventRequests(Long userId, Long eventId) {
        return requestRep.findByEvent_Id(eventId).stream().map(RequestMapper::toParticipationResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult changeRequestsStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest request) {
        final Event event = eventRep.findByIdAndInitiator_Id(eventId, userId).orElseThrow(() -> new NotFoundException(
                String.format("В базе данных нет события с id: %s и владельцем с id: %s", eventId, userId)));
        if (event.getParticipantLimit() != 0 && event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new DataIntegrityViolationException(String.format("Ошибка подтверждения заявок - " +
                    "достигнут лимит, participantLimit: %d", event.getParticipantLimit()));
        }
        if (event.getRequestModeration()) {
            for (Long id : request.getRequestIds()) {
                final Request req = requestRep.findByIdAndEvent_Id(id, eventId).orElseThrow(() -> new NotFoundException(
                        String.format("В базе данных нет заявки с id: %s и событием с id: %s", id, eventId)));
                if (req.getStatus() == RequestStatus.PENDING) {
                    if (request.getStatus() == RequestStatus.REJECTED) {
                        req.setStatus(RequestStatus.REJECTED);
                        requestRep.save(req);
                        log.debug("В БД изменен статус заявки: {}", req);
                    } else if (event.getConfirmedRequests().equals(event.getParticipantLimit())) {
                        req.setStatus(RequestStatus.REJECTED);
                        requestRep.save(req);
                        log.debug("В БД изменен статус заявки: {}", req);
                    } else if (request.getStatus() == RequestStatus.CONFIRMED) {
                        req.setStatus(RequestStatus.CONFIRMED);
                        requestRep.save(req);
                        log.debug("В БД изменен статус заявки: {}", req);
                        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                    }
                } else {
                    throw new DataIntegrityViolationException(String.format("Ошибка подтверждения заявок - " +
                            "нельзя подтвердить заявку в статусе: %s", req.getStatus()));
                }
            }
        }
        eventRep.save(event);
        log.debug("В БД обновлено поле сonfirmedRequests event: {}", event);
        return new EventRequestStatusUpdateResult(
                requestRep.findByEvent_IdAndStatus(eventId, RequestStatus.CONFIRMED).stream()
                        .map(RequestMapper::toParticipationResponseDto).collect(Collectors.toList()),
                requestRep.findByEvent_IdAndStatus(eventId, RequestStatus.REJECTED).stream()
                        .map(RequestMapper::toParticipationResponseDto).collect(Collectors.toList()));
    }

    private ParticipationResponseDto buildRequest(Event event, User user) {
        final Request response;
        if (event.getRequestModeration() && event.getParticipantLimit() != 0) {
            response = requestRep.save(new Request(null, LocalDateTime.now().withNano(0),
                    event, user, RequestStatus.PENDING));
        } else {
            response = requestRep.save(new Request(null, LocalDateTime.now().withNano(0),
                    event, user, RequestStatus.CONFIRMED));
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRep.save(event);
        }
        log.debug("В БД сохранен request: {}", response);
        return RequestMapper.toParticipationResponseDto(response);
    }
}
