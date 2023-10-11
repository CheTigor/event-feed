package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.category.model.Category;
import ru.practicum.event.dto.Location;
import ru.practicum.event.enums.EventStateUserAction;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventUserRequestDto {

    @Size(min = 20, max = 2000)
    private String annotation;
    private Category category;
    @Size(min = 20, max = 7000)
    private String description;
    private String eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private EventStateUserAction stateAction;
    @Size(min = 3, max = 120)
    private String title;
}
