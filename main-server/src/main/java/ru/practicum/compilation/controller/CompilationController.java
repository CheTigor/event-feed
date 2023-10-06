package ru.practicum.compilation.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationRequestDto;
import ru.practicum.compilation.dto.CompilationResponseDto;
import ru.practicum.compilation.dto.CompilationUpdateRequestDto;
import ru.practicum.compilation.service.CompilationServiceImpl;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Slf4j
@Valid
public class CompilationController {

    private final CompilationServiceImpl service;

    @Autowired
    public CompilationController(CompilationServiceImpl service) {
        this.service = service;
    }

    @PostMapping("/admin/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationResponseDto createCompilation(@RequestBody @Valid CompilationRequestDto request) {
        log.info("POST запрос createCompilation - request: \n{}", request);
        final CompilationResponseDto response = service.createCompilation(request);
        log.info("POST ответ createCompilation - response: \n{}", response);
        return response;
    }

    @DeleteMapping("/admin/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable(name = "compId") @Min(1) Long compId) {
        log.info("DELETE запрос deleteCompilation - compId: \n{}", compId);
        service.deleteCompilation(compId);
        log.info("Подборка с id: {} успешно удалено", compId);
    }

    @PatchMapping("/admin/compilations/{compId}")
    public CompilationResponseDto updateCompilation(@RequestBody @Valid CompilationUpdateRequestDto request,
                                                    @PathVariable(name = "compId") @Min(1) Long compId) {
        log.info("PATCH запрос updateCompilation - compId: \n{}, request: \n{}", compId, request);
        final CompilationResponseDto response = service.updateCompilation(request, compId);
        log.info("PATCH ответ updateCompilation - response: \n{}", response);
        return response;
    }

    @GetMapping("/compilations")
    public List<CompilationResponseDto> getCompsByParams(@RequestParam(name = "pinned", required = false) Boolean pinned,
                                                         @RequestParam(name = "from", defaultValue = "0") @Min(0) Integer from,
                                                         @RequestParam(name = "size", defaultValue = "10") @Min(1) Integer size) {
        log.info("GET запрос getCompsByParams - pinned: \n{}, from: \n{}, size: \n{}", pinned, from, size);
        final List<CompilationResponseDto> response = service.getCompsPublic(pinned, from, size);
        log.info("GET ответ getCompsByParams - response: \n{}", response);
        return response;
    }

    @GetMapping("/compilations/{compId}")
    public CompilationResponseDto getCompById(@PathVariable(name = "compId") @Min(1) Long compId) {
        log.info("GET запрос getCompById - compId: \n{}", compId);
        final CompilationResponseDto response = service.getCompById(compId);
        log.info("GET ответ getCompById - response: \n{}", response);
        return response;
    }
}
