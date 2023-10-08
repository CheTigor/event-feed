package ru.practicum.compilation.mapper;

import ru.practicum.compilation.dto.CompilationRequestDto;
import ru.practicum.compilation.dto.CompilationResponseDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.model.Event;

import java.util.List;
import java.util.Set;

public class CompilationMapper {

    public static CompilationResponseDto toCompilationResponseDto(Compilation compilation) {
        return new CompilationResponseDto(compilation.getId(), compilation.getEvents(), compilation.getPinned(),
                compilation.getTitle());
    }

    public static Compilation toCompilation(CompilationRequestDto request, List<Event> events) {
        return new Compilation(null, Set.copyOf(events), request.getPinned(), request.getTitle());
    }
}
