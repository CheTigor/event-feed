package ru.practicum.comments.mapper;

import ru.practicum.comments.dto.CommentFullResponseDto;
import ru.practicum.comments.dto.CommentShortResponseDto;
import ru.practicum.comments.model.Comment;

public class CommentMapper {

    public static CommentFullResponseDto toFullResponse(Comment comment) {
        return new CommentFullResponseDto(comment.getId(), comment.getText(), comment.getEventId(), comment.getUserId(),
                comment.getCreated(), comment.getItsResponse(), comment.getResponses());
    }

    public static CommentShortResponseDto toShortResponse(Comment comment) {
        return new CommentShortResponseDto(comment.getText(), comment.getUserId(), comment.getCreated(),
                comment.getResponses());
    }
}
