package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationRequestDto;
import ru.practicum.compilation.dto.CompilationResponseDto;
import ru.practicum.compilation.dto.CompilationUpdateRequestDto;

import java.util.List;

public interface CompilationService {

    CompilationResponseDto createCompilation(CompilationRequestDto request);

    void deleteCompilation(Long compId);

    CompilationResponseDto updateCompilation(CompilationUpdateRequestDto request, Long compId);

    List<CompilationResponseDto> getCompsPublic(Boolean pinned, Integer from, Integer size);

    CompilationResponseDto getCompById(Long compId);
}
