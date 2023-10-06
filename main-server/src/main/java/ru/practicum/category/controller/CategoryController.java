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
import java.util.List;

@Valid
@Slf4j
@RestController
public class CategoryController {

    private final CategoryServiceImpl categoryService;

    @Autowired
    public CategoryController(CategoryServiceImpl categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping(value = "/admin/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponseDto createCategory(@RequestBody @Valid CategoryRequestDto category) {
        log.info("POST запрос createCategory - category: \n{}", category);
        final CategoryResponseDto response = categoryService.createCategory(category);
        log.info("POST ответ createCategory - response: \n{}", response);
        return response;
    }

    @DeleteMapping(value = "/admin/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable(value = "catId") @Min(1) Long catId) {
        log.info("DELETE запрос deleteCategory - catId: \n{}", catId);
        categoryService.deleteCategory(catId);
        log.info("DELETE запрос deleteCategory для catId = {} выполнен успешно", catId);
    }

    @PatchMapping(value = "/admin/categories/{catId}")
    public CategoryResponseDto updateCategory(@RequestBody @Valid CategoryRequestDto category,
                                              @PathVariable(value = "catId") @Min(1) Long catId) {
        log.info("PATCH запрос updateCategory - catId: \n{}, category: \n{}", catId, category);
        final CategoryResponseDto response = categoryService.updateCategory(catId, category);
        log.info("PATCH ответ updateCategory - response: \n{}", response);
        return response;
    }

    @GetMapping("/categories")
    public List<CategoryResponseDto> getAllCategories(@RequestParam(value = "from", defaultValue = "0") @Min(0) Integer from,
                                                      @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size) {
        log.info("GET запрос getAllCategories - from: \n{}, size: \n{}", from, size);
        final List<CategoryResponseDto> response = categoryService.getAllCategories(from, size);
        log.info("GET ответ getAllCategories - response: \n{}", response);
        return response;
    }

    @GetMapping("/categories/{catId}")
    public CategoryResponseDto getCategoryById(@PathVariable(value = "catId") @Min(1) Long catId) {
        log.info("GET запрос getCategory - catId: \n{}", catId);
        final CategoryResponseDto response = categoryService.getCategoryById(catId);
        log.info("GET ответ getCategory - response: \n{}", response);
        return response;
    }
}
