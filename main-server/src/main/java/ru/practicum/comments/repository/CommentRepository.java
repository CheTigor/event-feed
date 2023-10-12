package ru.practicum.comments.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.comments.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByUserId(Long userId, PageRequest page);

    Page<Comment> findByEventId(Long eventId, PageRequest page);

    Page<Comment> findByEventIdAndItsResponse(Long eventId, Boolean itsResponse, PageRequest page);
}
