package ru.practicum.comments.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentShortResponseDto;
import ru.practicum.comments.service.CommentService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/events/{eventId}/comments")
public class CommentPublicController {

    private final CommentService service;

    @Autowired
    public CommentPublicController(CommentService service) {
        this.service = service;
    }

    @GetMapping
    public List<CommentShortResponseDto> getCommentsPublic(@PathVariable(name = "eventId") @Min(1) Long eventId,
                                                           @RequestParam(name = "from", defaultValue = "0") @Min(0) Integer from,
                                                           @RequestParam(name = "size", defaultValue = "10") @Min(1) Integer size) {
        log.info("GET запрос getCommentsPublic - eventId: \n{}", eventId);
        final List<CommentShortResponseDto> response = service.getCommentsPublic(eventId, from, size);
        log.info("GET ответ getCommentsPublic - response: \n{}", response);
        return response;
    }
}
