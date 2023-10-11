package ru.practicum.comments.service;

import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentFullResponseDto;
import ru.practicum.comments.dto.CommentRequestDto;
import ru.practicum.comments.dto.CommentShortResponseDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

public interface CommentService {

    CommentFullResponseDto getCommentByAdmin(Long commentId);

    CommentFullResponseDto updateCommentByAdmin(Long commentId, CommentRequestDto request);

    void deleteCommentByAdmin(Long commentId);

    CommentFullResponseDto createComment(Long userId, Long eventId, CommentRequestDto request, Long responseToId);

    List<CommentFullResponseDto> getAllCommentsByUser(Long userId, Integer from, Integer size);

    CommentFullResponseDto getCommentByUser(Long userId, Long commentId);

    CommentFullResponseDto updateCommentByUser(Long userId, Long commentId, CommentRequestDto request);

    void deleteCommentByUser(Long userId, Long commentId);

    List<CommentShortResponseDto> getCommentsPublic(Long eventId, Integer from, Integer size);
}
