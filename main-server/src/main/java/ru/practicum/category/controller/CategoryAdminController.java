package ru.practicum.category.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryRequestDto;
import ru.practicum.category.dto.CategoryResponseDto;
import ru.practicum.category.service.CategoryServiceImpl;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Valid
@Slf4j
@RestController
@RequestMapping("/admin/categories")
public class CategoryAdminController {

    private final CategoryServiceImpl categoryService;

    @Autowired
    public CategoryAdminController(CategoryServiceImpl categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponseDto createCategory(@RequestBody @Valid CategoryRequestDto category) {
        log.info("POST запрос createCategory - category: \n{}", category);
        final CategoryResponseDto response = categoryService.createCategory(category);
        log.info("POST ответ createCategory - response: \n{}", response);
        return response;
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable(value = "catId") @Min(1) Long catId) {
        log.info("DELETE запрос deleteCategory - catId: \n{}", catId);
        categoryService.deleteCategory(catId);
        log.info("DELETE запрос deleteCategory для catId = {} выполнен успешно", catId);
    }

    @PatchMapping("/{catId}")
    public CategoryResponseDto updateCategory(@RequestBody @Valid CategoryRequestDto category,
                                              @PathVariable(value = "catId") @Min(1) Long catId) {
        log.info("PATCH запрос updateCategory - catId: \n{}, category: \n{}", catId, category);
        final CategoryResponseDto response = categoryService.updateCategory(catId, category);
        log.info("PATCH ответ updateCategory - response: \n{}", response);
        return response;
    }
}
