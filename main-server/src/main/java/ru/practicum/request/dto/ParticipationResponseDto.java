package ru.practicum.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.request.enums.RequestStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationResponseDto {

    private Long id;
    private LocalDateTime created;
    private Long event;
    private Long requester;
    private RequestStatus status;

}
