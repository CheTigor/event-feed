package ru.practicum.category.mapper;

import ru.practicum.category.dto.CategoryRequestDto;
import ru.practicum.category.dto.CategoryResponseDto;
import ru.practicum.category.model.Category;

public class CategoryMapper {

    public static Category toCategory(Long id, CategoryRequestDto requestDto) {
        return new Category(id, requestDto.getName());
    }

    public static CategoryResponseDto toCategoryResponseDto(Category category) {
        return new CategoryResponseDto(category.getId(), category.getName());
    }
}
