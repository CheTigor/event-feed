package ru.practicum.event.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.*;
import ru.practicum.event.enums.EventState;
import ru.practicum.event.enums.Sorts;
import ru.practicum.event.model.EventAdminParams;
import ru.practicum.event.model.EventPublicParams;
import ru.practicum.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@Valid
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping(value = "/users/{userId}/events")
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

    @PostMapping(value = "/users/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventResponseDto createEvent(@RequestBody @Valid EventRequestDto eventRequestDto,
                                        @PathVariable("userId") @Min(1) Long userId) {
        log.info("POST запрос createEvent - userId: \n{}, eventRequest: \n{}", userId, eventRequestDto);
        final EventResponseDto response = eventService.createEvent(userId, eventRequestDto);
        log.info("POST ответ createEvent - response: \n{}", response);
        return response;
    }

    @GetMapping(value = "/users/{userId}/events/{eventId}")
    public EventResponseDto getEventById(@PathVariable("userId") @Min(1) Long userId,
                                         @PathVariable("eventId") @Min(1) Long eventId, HttpServletRequest request) {
        log.info("GET запрос getEventById - userId: \n{}, eventId: \n{}", userId, eventId);
        final EventResponseDto response = eventService.getEventById(userId, eventId, request);
        log.info("GET ответ getEventById - response: \n{}", response);
        return response;
    }

    @PatchMapping(value = "/users/{userId}/events/{eventId}")
    public EventResponseDto updateEventByUser(@RequestBody @Valid UpdateEventUserRequestDto event,
                                              @PathVariable("userId") @Min(1) Long userId,
                                              @PathVariable("eventId") @Min(1) Long eventId) {
        log.info("PATCH запрос updateEventByUser - userId: \n{}, eventId: \n{}, event: \n{}", userId, eventId, event);
        final EventResponseDto response = eventService.updateEventByUser(event, userId, eventId);
        log.info("PATCH ответ updateEventByUser - response: \n{}", response);
        return response;
    }

    @GetMapping("/admin/events")
    public List<EventResponseDto> getAdminEventsByParams(
            @RequestParam(value = "users", required = false) List<Long> usersId,
            @RequestParam(value = "states", required = false) List<EventState> states,
            @RequestParam(value = "categories", required = false) List<Long> categoriesId,
            @RequestParam(value = "rangeStart", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime rangeStart,
            @RequestParam(value = "rangeEnd", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime rangeEnd,
            @RequestParam(value = "from", defaultValue = "0") @Min(0) Integer from,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size) {
        final EventAdminParams params = new EventAdminParams(usersId, states, categoriesId, rangeStart, rangeEnd);
        log.info("GET запрос getEventsByParams - params: \n{}", params);
        final List<EventResponseDto> response = eventService.getAdminEventsByParams(params, from, size);
        log.info("GET ответ getEventsByParams - response: \n{}", response);
        return response;
    }

    @PatchMapping(value = "/admin/events/{eventId}")
    public EventResponseDto updateEventByAdmin(@RequestBody @Valid UpdateEventAdminRequestDto event,
                                               @PathVariable("eventId") @Min(1) Long eventId) {
        log.info("PATCH запрос updateEventByAdmin - eventId: \n{}, event: \n{}", eventId, event);
        final EventResponseDto response = eventService.updateEventByAdmin(event, eventId);
        log.info("PATCH ответ updateEventByAdmin - response: \n{}", response);
        return response;
    }

    @GetMapping("/events")
    public List<EventShortResponseDto> getPublicEventsByParams(
            @RequestParam(value = "text", required = false) String text,
            @RequestParam(value = "categories", required = false) List<Long> categories,
            @RequestParam(value = "paid", required = false) Boolean paid,
            @RequestParam(value = "onlyAvailable", required = false) Boolean onlyAvailable,
            @RequestParam(value = "sort", required = false) Sorts sort,
            @RequestParam(value = "rangeStart", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime rangeStart,
            @RequestParam(value = "rangeEnd", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime rangeEnd,
            @RequestParam(value = "from", defaultValue = "0") @Min(0) Integer from,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size,
            HttpServletRequest request) {
        final EventPublicParams params = new EventPublicParams(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort);
        log.info("GET запрос getPublicEventsByParams - params: \n{}, from: \n{}, size: \n{}", params, from, size);
        final List<EventShortResponseDto> response = eventService.getPublicEventsByParams(params, from, size, request);
        log.info("GET ответ getPublicEventsByParams - response: \n{}", response);
        return response;
    }

    @GetMapping("/events/{id}")
    public EventResponseDto getPublicEventById(@PathVariable(value = "id") @Min(1) Long id, HttpServletRequest request) {
        log.info("GET запрос getPublicEventById - id: \n{}", id);
        final EventResponseDto response = eventService.getPublicEventById(id, request);
        log.info("GET ответ getPublicEventById - response: \n{}", response);
        return response;
    }

}
