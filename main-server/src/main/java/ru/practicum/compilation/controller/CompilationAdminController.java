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

@RestController
@Slf4j
@Valid
@RequestMapping("/admin/compilations")
public class CompilationAdminController {

    private final CompilationServiceImpl service;

    @Autowired
    public CompilationAdminController(CompilationServiceImpl service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationResponseDto createCompilation(@RequestBody @Valid CompilationRequestDto request) {
        log.info("POST запрос createCompilation - request: \n{}", request);
        final CompilationResponseDto response = service.createCompilation(request);
        log.info("POST ответ createCompilation - response: \n{}", response);
        return response;
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable(name = "compId") @Min(1) Long compId) {
        log.info("DELETE запрос deleteCompilation - compId: \n{}", compId);
        service.deleteCompilation(compId);
        log.info("Подборка с id: {} успешно удалено", compId);
    }

    @PatchMapping("/{compId}")
    public CompilationResponseDto updateCompilation(@RequestBody @Valid CompilationUpdateRequestDto request,
                                                    @PathVariable(name = "compId") @Min(1) Long compId) {
        log.info("PATCH запрос updateCompilation - compId: \n{}, request: \n{}", compId, request);
        final CompilationResponseDto response = service.updateCompilation(request, compId);
        log.info("PATCH ответ updateCompilation - response: \n{}", response);
        return response;
    }
}
