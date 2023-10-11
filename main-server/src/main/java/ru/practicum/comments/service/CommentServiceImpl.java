package ru.practicum.comments.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.comments.dto.CommentFullResponseDto;
import ru.practicum.comments.dto.CommentRequestDto;
import ru.practicum.comments.dto.CommentShortResponseDto;
import ru.practicum.comments.mapper.CommentMapper;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.repository.CommentRepository;
import ru.practicum.event.enums.EventState;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository comRep;
    private final UserRepository userRep;
    private final EventRepository eventRep;

    @Autowired
    public CommentServiceImpl(CommentRepository comRep, UserRepository userRep, EventRepository eventRep) {
        this.comRep = comRep;
        this.userRep = userRep;
        this.eventRep = eventRep;
    }

    @Override
    public CommentFullResponseDto getCommentByAdmin(Long commentId) {
        return CommentMapper.toFullResponse(comRep.findById(commentId).orElseThrow(() -> new NotFoundException(
                String.format("В базе данных нет комментария с id - %s", commentId))));
    }

    @Override
    public CommentFullResponseDto updateCommentByAdmin(Long commentId, CommentRequestDto request) {
        final Comment comment = comRep.findById(commentId).orElseThrow(() -> new NotFoundException(
                String.format("В базе данных нет комментария с id - %s", commentId)));
        comment.setText(request.getText());
        final Comment updComment = comRep.save(comment);
        log.debug("В БД успешно обновлен комментарий: {}", updComment);
        return CommentMapper.toFullResponse(updComment);
    }

    @Override
    public void deleteCommentByAdmin(Long commentId) {
        if (comRep.existsById(commentId)) {
            comRep.deleteById(commentId);
        } else {
            throw new NotFoundException(String.format("В базе данных нет комментария с id - %s", commentId));
        }
    }

    @Override
    public CommentFullResponseDto createComment(Long userId, Long eventId, CommentRequestDto request, Long responseToId) {
        userValidate(userId);
        final Event event = eventRep.findById(eventId).orElseThrow(() -> new NotFoundException(String.format(
                "В базе данных нет событие с id - %d", eventId)));
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ValidationException("Комментарий не может быть добавлен для неопубликованного события");
        }
        final Comment comment = comRep.save(new Comment(null, request.getText(), eventId, userId, LocalDateTime.now(),
                false, List.of()));
        if (responseToId != null) {
            final Comment mainCom = comRep.findById(responseToId).orElseThrow(() -> new NotFoundException(
                    String.format("В базе данных нет комментария с id - %d", responseToId)));
            if (!mainCom.getEventId().equals(eventId)) {
                throw new ValidationException(String.format("Ошибка создания комментария - ответ на комментарий должен " +
                        "быть с тем же eventId что и основной комментарий: expected - %s, current - %s",
                        comment.getEventId(), eventId));
            }
            comment.setItsResponse(true);
            mainCom.getResponses().add(comment);
            comRep.save(mainCom);
        }
        return CommentMapper.toFullResponse(comment);
    }

    @Override
    public List<CommentFullResponseDto> getAllCommentsByUser(Long userId, Integer from, Integer size) {
        userValidate(userId);
        return comRep.findByUserId(userId, PageRequest.of(from/size, size)).stream()
                .map(CommentMapper::toFullResponse).collect(Collectors.toList());
    }

    @Override
    public CommentFullResponseDto getCommentByUser(Long userId, Long commentId) {
        final Comment comment = commentValidate(userId, commentId);
        return CommentMapper.toFullResponse(comment);
    }

    @Override
    public CommentFullResponseDto updateCommentByUser(Long userId, Long commentId, CommentRequestDto request) {
        final Comment comment = commentValidate(userId, commentId);
        comment.setText(request.getText());
        final Comment updComment = comRep.save(comment);
        log.debug("В БД успешно обновлен comment: {}", updComment);
        return CommentMapper.toFullResponse(updComment);
    }

    @Override
    public void deleteCommentByUser(Long userId, Long commentId) {
        final Comment comment = commentValidate(userId, commentId);
        if (!comment.getResponses().isEmpty()) {
            for (Comment curCom : comment.getResponses()) {
                curCom.setItsResponse(false);
                comRep.save(curCom);
            }
        }
        comRep.deleteById(commentId);
    }

    @Override
    public List<CommentShortResponseDto> getCommentsPublic(Long eventId, Integer from, Integer size) {
        if (!eventRep.existsById(eventId)) {
            throw new NotFoundException(String.format("В базе данных нет событие с id - %d", eventId));
        }
        return comRep.findByEventId(eventId, PageRequest.of(from/size, size)).stream()
                .map(CommentMapper::toShortResponse).collect(Collectors.toList());
    }

    private void userValidate(Long userId) {
        if (!userRep.existsById(userId)) {
            throw new NotFoundException(String.format("В базе данных нет пользователя с id - %d", userId));
        }
    }

    private Comment commentValidate(Long userId, Long commentId) {
        userValidate(userId);
        final Comment comment = comRep.findById(commentId).orElseThrow(() -> new NotFoundException(
                String.format("В базе данных нет комментария с id - %d", commentId)));
        if (!comment.getUserId().equals(userId)) {
            throw new ValidationException(String.format("Невозможно получить полную информацию о комментарии - " +
                    "пользователь с id: %d не является автором комментария с id: %d", userId, commentId));
        }
        return comment;
    }

}
