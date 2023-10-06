package ru.practicum.request.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationResponseDto;
import ru.practicum.request.service.RequestServiceImpl;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Slf4j
public class RequestController {

    private final RequestServiceImpl service;

    @Autowired
    public RequestController(RequestServiceImpl service) {
        this.service = service;
    }

    @GetMapping("/users/{userId}/requests")
    public List<ParticipationResponseDto> getUserRequests(@PathVariable(name = "userId") @Min(1) Long userId) {
        log.info("GET запрос getUserRequests - userId: \n{}", userId);
        final List<ParticipationResponseDto> response = service.getUserRequests(userId);
        log.info("GET ответ getUserRequests - response: \n{}", response);
        return response;
    }

    @PostMapping("/users/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationResponseDto createRequest(@PathVariable(name = "userId") @Min(1) Long userId,
                                                  @RequestParam(name = "eventId") @Min(1) Long eventId) {
        log.info("POST запрос createRequest - userId: \n{}, eventId: \n{}", userId, eventId);
        final ParticipationResponseDto response = service.createRequest(userId, eventId);
        log.info("POST ответ createRequest - response: \n{}", response);
        return response;
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public ParticipationResponseDto cancelRequestByUser(@PathVariable(name = "userId") @Min(1) Long userId,
                                                        @PathVariable(name = "requestId") @Min(1) Long requestId) {
        log.info("PATCH запрос cancelRequestByUser - userId: \n{}, requestId: \n{}", userId, requestId);
        final ParticipationResponseDto response = service.cancelRequestByUser(userId, requestId);
        log.info("PATCH ответ cancelRequestByUser - response: \n{}", response);
        return response;
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public List<ParticipationResponseDto> getEventRequests(@PathVariable(name = "userId") @Min(1) Long userId,
                                                           @PathVariable(name = "eventId") @Min(1) Long eventId) {
        log.info("GET запрос getEventRequests - userId: \n{}, eventId: \n{}", userId, eventId);
        final List<ParticipationResponseDto> response = service.getEventRequests(userId, eventId);
        log.info("GET ответ getEventRequests - response: \n{}", response);
        return response;
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult changeRequestsStatus(@RequestBody EventRequestStatusUpdateRequest request,
                                                               @PathVariable(name = "userId") @Min(0) Long userId,
                                                               @PathVariable(name = "eventId") @Min(1) Long eventId) {
        log.info("PATCH запрос changeRequestsStatus - userId: \n{}, eventId: \n{}, request: \n{}", userId, eventId, request);
        final EventRequestStatusUpdateResult response = service.changeRequestsStatus(userId, eventId, request);
        log.info("PATCH ответ changeRequestsStatus - response: \n{}", response);
        return response;
    }
}
