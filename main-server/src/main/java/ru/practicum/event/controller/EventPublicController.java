package ru.practicum.event.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventPublicParams;
import ru.practicum.event.dto.EventResponseDto;
import ru.practicum.event.dto.EventShortResponseDto;
import ru.practicum.event.enums.Sorts;
import ru.practicum.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@Valid
@RequestMapping("/events")
public class EventPublicController {

    private final EventService eventService;

    @Autowired
    public EventPublicController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
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

    @GetMapping("/{id}")
    public EventResponseDto getPublicEventById(@PathVariable(value = "id") @Min(1) Long id, HttpServletRequest request) {
        log.info("GET запрос getPublicEventById - id: \n{}", id);
        final EventResponseDto response = eventService.getPublicEventById(id, request);
        log.info("GET ответ getPublicEventById - response: \n{}", response);
        return response;
    }

}
