package ru.practicum.category.service;

import ru.practicum.category.dto.CategoryRequestDto;
import ru.practicum.category.dto.CategoryResponseDto;

import java.util.List;


public interface CategoryService {

    CategoryResponseDto createCategory(CategoryRequestDto category);

    void deleteCategory(Long catId);

    CategoryResponseDto updateCategory(Long catId, CategoryRequestDto category);

    List<CategoryResponseDto> getAllCategories(Integer from, Integer size);

    CategoryResponseDto getCategoryById(Long catId);
}
