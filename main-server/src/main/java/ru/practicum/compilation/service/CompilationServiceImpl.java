package ru.practicum.compilation.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.dto.CompilationRequestDto;
import ru.practicum.compilation.dto.CompilationResponseDto;
import ru.practicum.compilation.dto.CompilationUpdateRequestDto;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.NotFoundException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compRep;
    private final EventRepository eventRep;

    @Autowired
    public CompilationServiceImpl(CompilationRepository compRep, EventRepository eventRep) {
        this.compRep = compRep;
        this.eventRep = eventRep;
    }

    @Override
    @Transactional
    public CompilationResponseDto createCompilation(CompilationRequestDto request) {
        List<Event> events = new ArrayList<>();
        if (request.getEvents() != null) {
            if (!request.getEvents().isEmpty())
                events = eventRep.findAllById(request.getEvents());
        }
        final Compilation compilation = compRep.save(CompilationMapper.toCompilation(request, events));
        log.debug("В БД сохранена подборка: {}", compilation);
        return CompilationMapper.toCompilationResponseDto(compilation);
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compId) {
        if (compRep.existsById(compId)) {
            compRep.deleteById(compId);
            log.debug("Из БД удалена подборка с id: {}", compId);
        } else {
            throw new NotFoundException(String.format("В базе данных нет подборки с id - %s", compId));
        }
    }

    @Override
    @Transactional
    public CompilationResponseDto updateCompilation(CompilationUpdateRequestDto request, Long compId) {
        final Compilation compilation = compRep.findById(compId).orElseThrow(() -> new NotFoundException(
                String.format("В базе данных нет подборки с id - %s", compId)));
        if (request.getEvents() != null) {
            if (!request.getEvents().isEmpty()) {
                if (request.getEvents().get(0) != 0) {
                    compilation.setEvents(new HashSet<>(eventRep.findAllById(request.getEvents())));
                }
            }
        }
        if (request.getPinned() != null) {
            compilation.setPinned(request.getPinned());
        }
        if (request.getTitle() != null) {
            if (!request.getTitle().isBlank()) {
                compilation.setTitle(request.getTitle());
            }
        }
        final Compilation updatedComp = compRep.save(compilation);
        log.debug("В БД обновлена подборка: {}", updatedComp);
        return CompilationMapper.toCompilationResponseDto(updatedComp);
    }

    @Override
    public List<CompilationResponseDto> getCompsPublic(Boolean pinned, Integer from, Integer size) {
        if (pinned != null) {
            return compRep.findByPinned(pinned, PageRequest.of(from / size, size)).stream()
                    .map(CompilationMapper::toCompilationResponseDto).collect(Collectors.toList());
        } else {
            return compRep.findAll(PageRequest.of(from / size, size)).stream()
                    .map(CompilationMapper::toCompilationResponseDto).collect(Collectors.toList());
        }
    }

    @Override
    public CompilationResponseDto getCompById(Long compId) {
        return CompilationMapper.toCompilationResponseDto(compRep.findById(compId).orElseThrow(() -> new NotFoundException(
                String.format("В базе данных нет подборки с id - %s", compId))));
    }
}
