package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.event.model.Event;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompilationResponseDto {

    private Long id;

    private Set<Event> events;

    private Boolean pinned;

    private String title;
}
