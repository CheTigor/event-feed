package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.category.model.Category;
import ru.practicum.event.enums.EventState;
import ru.practicum.event.model.Location;
import ru.practicum.user.model.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventResponseDto {

    private Long id;
    private String annotation;
    private Category category;
    private Integer confirmedRequests;
    private String createdOn;
    private String description;
    private String eventDate;
    private User initiator;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private String publishedOn;
    private Boolean requestModeration;
    private EventState state;
    private String title;
    private Long views;
}
