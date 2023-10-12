package ru.practicum.comments.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentFullResponseDto;
import ru.practicum.comments.dto.CommentRequestDto;
import ru.practicum.comments.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Valid
@Slf4j
@RequestMapping("/users/{userId}/comments")
public class CommentPrivateController {

    private final CommentService service;

    @Autowired
    public CommentPrivateController(CommentService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentFullResponseDto createComment(@RequestBody @Valid CommentRequestDto request,
                                                @PathVariable(name = "userId") @Min(1) Long userId,
                                                @RequestParam(name = "eventId") @Min(1) Long eventId,
                                                @RequestParam(name = "responseToId", required = false)
                                                @Min(1) Long responseToId) {
        log.info("POST запрос createComment - userId: \n{}, eventId: \n{}, request: \n{}", userId, eventId, request);
        final CommentFullResponseDto response = service.createComment(userId, eventId, request, responseToId);
        log.info("POST ответ createComment - response: \n{}", response);
        return response;
    }

    @GetMapping
    public List<CommentFullResponseDto> getAllCommentsByUser(@PathVariable(name = "userId") @Min(1) Long userId,
                                                             @RequestParam(name = "from", defaultValue = "0")
                                                             @Min(0) Integer from,
                                                             @RequestParam(name = "size", defaultValue = "10")
                                                             @Min(1) Integer size) {
        log.info("GET запрос getAllCommentsByUser - userId: \n{}", userId);
        final List<CommentFullResponseDto> response = service.getAllCommentsByUser(userId, from, size);
        log.info("GET ответ getAllCommentsByUser - response: \n{}", response);
        return response;
    }

    @GetMapping("/{commentId}")
    public CommentFullResponseDto getCommentByUser(@PathVariable(name = "userId") @Min(1) Long userId,
                                                   @PathVariable(name = "commentId") @Min(1) Long commentId) {
        log.info("GET запрос getCommentByUser - userId: \n{}, commentId: \n{}", userId, commentId);
        final CommentFullResponseDto response = service.getCommentByUser(userId, commentId);
        log.info("GET ответ getCommentByUser - response: \n{}", response);
        return response;
    }

    @PatchMapping("/{commentId}")
    public CommentFullResponseDto updateCommentByUser(@RequestBody @Valid CommentRequestDto request,
                                                      @PathVariable(name = "userId") @Min(1) Long userId,
                                                      @PathVariable(name = "commentId") @Min(1) Long commentId) {
        log.info("PATCH запрос updateCommentByUser - userId: \n{}, commentId: \n{}, request: \n{}",
                userId, commentId, request);
        final CommentFullResponseDto response = service.updateCommentByUser(userId, commentId, request);
        log.info("PATCH ответ updateCommentByUser - response: \n{}", response);
        return response;
    }

    @DeleteMapping("/{commentId}")
    public void deleteCommentByUser(@PathVariable(name = "userId") @Min(1) Long userId,
                                    @PathVariable(name = "commentId") @Min(1) Long commentId) {
        log.info("DELETE запрос deleteCommentByUser - userId: \n{}, commentId: \n{}", userId, commentId);
        service.deleteCommentByUser(userId, commentId);
        log.info("Comment с id: {} успешно удален", commentId);
    }
}
