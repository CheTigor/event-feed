package ru.practicum.compilation.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationResponseDto;
import ru.practicum.compilation.service.CompilationService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/compilations")
public class CompilationPublicController {

    private final CompilationService service;

    @Autowired
    public CompilationPublicController(CompilationService service) {
        this.service = service;
    }

    @GetMapping
    public List<CompilationResponseDto> getCompsByParams(@RequestParam(name = "pinned", required = false) Boolean pinned,
                                                         @RequestParam(name = "from", defaultValue = "0") @Min(0) Integer from,
                                                         @RequestParam(name = "size", defaultValue = "10") @Min(1) Integer size) {
        log.info("GET запрос getCompsByParams - pinned: \n{}, from: \n{}, size: \n{}", pinned, from, size);
        final List<CompilationResponseDto> response = service.getCompsPublic(pinned, from, size);
        log.info("GET ответ getCompsByParams - response: \n{}", response);
        return response;
    }

    @GetMapping("/{compId}")
    public CompilationResponseDto getCompById(@PathVariable(name = "compId") @Min(1) Long compId) {
        log.info("GET запрос getCompById - compId: \n{}", compId);
        final CompilationResponseDto response = service.getCompById(compId);
        log.info("GET ответ getCompById - response: \n{}", response);
        return response;
    }
}
