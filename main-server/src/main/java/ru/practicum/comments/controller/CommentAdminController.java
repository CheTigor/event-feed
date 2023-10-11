package ru.practicum.comments.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentFullResponseDto;
import ru.practicum.comments.dto.CommentRequestDto;
import ru.practicum.comments.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@Slf4j
@RequestMapping("/admin/comments")
public class CommentAdminController {

    private final CommentService service;

    @Autowired
    public CommentAdminController(CommentService service) {
        this.service = service;
    }

    @GetMapping("/{commentId}")
    public CommentFullResponseDto getCommentByAdmin(@PathVariable(name = "commentId") @Min(1) Long commentId) {
        log.info("GET запрос getCommentByAdmin - commentId: \n{}", commentId);
        final CommentFullResponseDto response = service.getCommentByAdmin(commentId);
        log.info("GET ответ getCommentByAdmin - response: \n{}", response);
        return response;
    }

    @PatchMapping("/{commentId}")
    public CommentFullResponseDto updateCommentByAdmin(@RequestBody @Valid CommentRequestDto request,
                                                       @PathVariable(name = "commentId") @Min(1) Long commentId) {
        log.info("GET запрос updateCommentByAdmin - commentId: \n{}, request: \n{}", commentId, request);
        final CommentFullResponseDto response = service.updateCommentByAdmin(commentId, request);
        log.info("GET ответ updateCommentByAdmin - response: \n{}", response);
        return response;
    }

    @DeleteMapping("/{commentId}")
    public void deleteCommentByAdmin(@PathVariable(name = "commentId") @Min(1) Long commentId) {
        log.info("GET запрос deleteCommentByAdmin - commentId: \n{}", commentId);
        service.deleteCommentByAdmin(commentId);
        log.info("Comment с id: {} успешно удален", commentId);
    }
}

