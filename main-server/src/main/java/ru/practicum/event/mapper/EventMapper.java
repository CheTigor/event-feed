package ru.practicum.event.mapper;

import ru.practicum.comments.model.Comment;
import ru.practicum.constants.Constants;
import ru.practicum.event.dto.*;
import ru.practicum.event.model.Event;
import ru.practicum.user.mapper.UserMapper;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class EventMapper {

    //В таком формате это читаемо
    public static EventShortResponseDto toEventShort(Event event) {
        return new EventShortResponseDto(
                event.getAnnotation(),
                event.getCategory(),
                event.getConfirmedRequests(),
                event.getEventDate() == null ? null : event.getEventDate()
                        .format(DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT)),
                event.getId(),
                UserMapper.toUserShort(event.getInitiator()),
                event.getPaid(),
                event.getTitle(),
                event.getViews());
    }

    public static EventResponseDto toEventResponse(Event event, List<Comment> comments) {
        return new EventResponseDto(
                event.getId(),
                event.getAnnotation(),
                event.getCategory(),
                event.getConfirmedRequests(),
                (event.getCreatedOn() == null) ? null : event.getCreatedOn()
                        .format(DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT)),
                event.getDescription(),
                event.getEventDate() == null ? null : event.getEventDate()
                        .format(DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT)),
                event.getInitiator(),
                new Location(event.getLocationLat(), event.getLocationLon()),
                event.getPaid(), event.getParticipantLimit(),
                event.getPublishedOn() == null ? null : event.getPublishedOn()
                        .format(DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT)),
                event.getRequestModeration(),
                event.getState(),
                event.getTitle(),
                event.getViews(),
                comments);
    }

    public static CommonUpdateParams toCommonUpdateParams(UpdateEventAdminRequestDto event) {
        return new CommonUpdateParams(event.getAnnotation(), event.getDescription(), event.getEventDate(), event.getLocation(),
                event.getPaid(), event.getParticipantLimit(), event.getRequestModeration(), event.getTitle());
    }

    public static CommonUpdateParams toCommonUpdateParams(UpdateEventUserRequestDto event) {
        return new CommonUpdateParams(event.getAnnotation(), event.getDescription(), event.getEventDate(), event.getLocation(),
                event.getPaid(), event.getParticipantLimit(), event.getRequestModeration(), event.getTitle());
    }
}
