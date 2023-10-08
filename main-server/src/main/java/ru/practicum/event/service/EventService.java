package ru.practicum.event.service;

import ru.practicum.event.dto.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {

    List<EventShortResponseDto> getEventsByUserId(Long userId, Integer from, Integer size, HttpServletRequest request);

    EventResponseDto createEvent(Long userId, EventRequestDto eventRequestDto);

    EventResponseDto getEventById(Long userId, Long eventId, HttpServletRequest request);

    EventResponseDto updateEventByUser(UpdateEventUserRequestDto event, Long userId, Long eventId);

    //List<ParticipationRepsonseDto> getEventsRequests(Long userId, Long eventId);

    //ParticipationRepsonseDto decideRequest(EventRequestStatusUpdateRequest request, Long userId, Long eventId);

    List<EventResponseDto> getAdminEventsByParams(EventAdminParams params, Integer from, Integer size);

    EventResponseDto updateEventByAdmin(UpdateEventAdminRequestDto event, Long eventId);

    List<EventShortResponseDto> getPublicEventsByParams(EventPublicParams params, Integer from, Integer size,
                                                        HttpServletRequest request);

    EventResponseDto getPublicEventById(Long id, HttpServletRequest request);
}
