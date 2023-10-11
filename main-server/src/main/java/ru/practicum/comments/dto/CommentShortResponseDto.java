package ru.practicum.comments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.comments.model.Comment;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentShortResponseDto {

    private String text;
    private Long userId;
    private LocalDateTime created;
    private List<Comment> responses;
}
