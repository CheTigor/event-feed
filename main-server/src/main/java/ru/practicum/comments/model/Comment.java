package ru.practicum.comments.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "text")
    private String text;
    @Column(name = "event_id")
    private Long eventId;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "created")
    private LocalDateTime created;
    @Column(name = "its_response")
    private Boolean itsResponse;
    @ManyToMany
    @JoinTable(name = "responses_comment",
            joinColumns = @JoinColumn(name = "main_comment_id"),
            inverseJoinColumns = @JoinColumn(name = "response_id"))
    private List<Comment> responses;
}
