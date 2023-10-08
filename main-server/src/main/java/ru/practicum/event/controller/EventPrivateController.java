package ru.practicum.event.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.*;
import ru.practicum.event.service.EventService;
import ru.practicum.request.dto.ParticipationResponseDto;
import ru.practicum.request.service.RequestService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Slf4j
@Valid
@RequestMapping("/users/{userId}/events")
public class EventPrivateController {

    private final EventService eventService;
    private final RequestService requestService;

    @Autowired
    public EventPrivateController(EventService eventService, RequestService requestService) {
        this.eventService = eventService;
        this.requestService = requestService;
    }

    @GetMapping
    public List<EventShortResponseDto> getEventsByUserId(@PathVariable("userId") @Min(1) Long userId,
                                                         @RequestParam(value = "from", defaultValue = "0") @Min(0) Integer from,
                                                         @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size,
                                                         HttpServletRequest request) {
        //int page = from/size
        log.info("GET запрос getEventsByUserId - userId: \n{}, from: \n{}, size: \n{}", userId, from, size);
        final List<EventShortResponseDto> response = eventService.getEventsByUserId(userId, from, size, request);
        log.info("GET ответ getEventsByUserId - response: \n{}", response);
        return response;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventResponseDto createEvent(@RequestBody @Valid EventRequestDto eventRequestDto,
                                        @PathVariable("userId") @Min(1) Long userId) {
        log.info("POST запрос createEvent - userId: \n{}, eventRequest: \n{}", userId, eventRequestDto);
        final EventResponseDto response = eventService.createEvent(userId, eventRequestDto);
        log.info("POST ответ createEvent - response: \n{}", response);
        return response;
    }

    @GetMapping("/{eventId}")
    public EventResponseDto getEventById(@PathVariable("userId") @Min(1) Long userId,
                                         @PathVariable("eventId") @Min(1) Long eventId, HttpServletRequest request) {
        log.info("GET запрос getEventById - userId: \n{}, eventId: \n{}", userId, eventId);
        final EventResponseDto response = eventService.getEventById(userId, eventId, request);
        log.info("GET ответ getEventById - response: \n{}", response);
        return response;
    }

    @PatchMapping("/{eventId}")
    public EventResponseDto updateEventByUser(@RequestBody @Valid UpdateEventUserRequestDto event,
                                              @PathVariable("userId") @Min(1) Long userId,
                                              @PathVariable("eventId") @Min(1) Long eventId) {
        log.info("PATCH запрос updateEventByUser - userId: \n{}, eventId: \n{}, event: \n{}", userId, eventId, event);
        final EventResponseDto response = eventService.updateEventByUser(event, userId, eventId);
        log.info("PATCH ответ updateEventByUser - response: \n{}", response);
        return response;
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationResponseDto> getEventRequests(@PathVariable(name = "userId") @Min(1) Long userId,
                                                           @PathVariable(name = "eventId") @Min(1) Long eventId) {
        log.info("GET запрос getEventRequests - userId: \n{}, eventId: \n{}", userId, eventId);
        final List<ParticipationResponseDto> response = requestService.getEventRequests(userId, eventId);
        log.info("GET ответ getEventRequests - response: \n{}", response);
        return response;
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult changeRequestsStatus(@RequestBody EventRequestStatusUpdateRequest request,
                                                               @PathVariable(name = "userId") @Min(0) Long userId,
                                                               @PathVariable(name = "eventId") @Min(1) Long eventId) {
        log.info("PATCH запрос changeRequestsStatus - userId: \n{}, eventId: \n{}, request: \n{}", userId, eventId, request);
        final EventRequestStatusUpdateResult response = requestService.changeRequestsStatus(userId, eventId, request);
        log.info("PATCH ответ changeRequestsStatus - response: \n{}", response);
        return response;
    }
}
