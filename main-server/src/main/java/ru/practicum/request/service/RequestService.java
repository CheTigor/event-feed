package ru.practicum.request.service;

import ru.practicum.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.event.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationResponseDto;

import java.util.List;

public interface RequestService {

    List<ParticipationResponseDto> getUserRequests(Long userId);

    ParticipationResponseDto createRequest(Long userId, Long eventId);

    ParticipationResponseDto cancelRequestByUser(Long userId, Long requestId);

    List<ParticipationResponseDto> getEventRequests(Long userId, Long eventId);

    EventRequestStatusUpdateResult changeRequestsStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest request);
}
