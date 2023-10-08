package ru.practicum.category.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryResponseDto;
import ru.practicum.category.service.CategoryService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/categories")
public class CategoryPublicController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryPublicController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<CategoryResponseDto> getAllCategories(@RequestParam(value = "from", defaultValue = "0") @Min(0) Integer from,
                                                      @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size) {
        log.info("GET запрос getAllCategories - from: \n{}, size: \n{}", from, size);
        final List<CategoryResponseDto> response = categoryService.getAllCategories(from, size);
        log.info("GET ответ getAllCategories - response: \n{}", response);
        return response;
    }

    @GetMapping("/{catId}")
    public CategoryResponseDto getCategoryById(@PathVariable(value = "catId") @Min(1) Long catId) {
        log.info("GET запрос getCategory - catId: \n{}", catId);
        final CategoryResponseDto response = categoryService.getCategoryById(catId);
        log.info("GET ответ getCategory - response: \n{}", response);
        return response;
    }
}
