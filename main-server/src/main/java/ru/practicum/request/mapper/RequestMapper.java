package ru.practicum.request.mapper;

import ru.practicum.request.dto.ParticipationResponseDto;
import ru.practicum.request.model.Request;

public class RequestMapper {

    public static ParticipationResponseDto toParticipationResponseDto(Request request) {
        return new ParticipationResponseDto(request.getId(), request.getCreated(), request.getEvent().getId(),
                request.getRequester().getId(), request.getStatus());
    }
}
