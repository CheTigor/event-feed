package ru.practicum.event.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventAdminParams;
import ru.practicum.event.dto.EventResponseDto;
import ru.practicum.event.dto.UpdateEventAdminRequestDto;
import ru.practicum.event.enums.EventState;
import ru.practicum.event.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@Valid
@RequestMapping("/admin/events")
public class EventAdminController {

    private final EventService eventService;

    @Autowired
    public EventAdminController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
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

    @PatchMapping("/{eventId}")
    public EventResponseDto updateEventByAdmin(@RequestBody @Valid UpdateEventAdminRequestDto event,
                                               @PathVariable("eventId") @Min(1) Long eventId) {
        log.info("PATCH запрос updateEventByAdmin - eventId: \n{}, event: \n{}", eventId, event);
        final EventResponseDto response = eventService.updateEventByAdmin(event, eventId);
        log.info("PATCH ответ updateEventByAdmin - response: \n{}", response);
        return response;
    }
}
