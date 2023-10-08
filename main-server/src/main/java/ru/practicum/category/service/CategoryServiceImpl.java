package ru.practicum.category.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryRequestDto;
import ru.practicum.category.dto.CategoryResponseDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.exception.NotFoundException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public CategoryResponseDto createCategory(CategoryRequestDto category) {
        final CategoryResponseDto newCategory = CategoryMapper.toCategoryResponseDto(repository.save(
                CategoryMapper.toCategory(null, category)));
        log.debug("В БД сохранена категория: {}", newCategory);
        return newCategory;
    }

    @Override
    @Transactional
    public void deleteCategory(Long catId) {
        repository.deleteById(catId);
        log.debug("Из БД удалена категория с id: {}", catId);
    }

    @Override
    @Transactional
    public CategoryResponseDto updateCategory(Long catId, CategoryRequestDto category) {
        if (!repository.existsById(catId)) {
            throw new NotFoundException(String.format("Category с id: %d не найдена", catId));
        }
        final CategoryResponseDto updCategory = CategoryMapper.toCategoryResponseDto(repository.save(
                CategoryMapper.toCategory(catId, category)));
        log.debug("В БД обновлена категория: {}", updCategory);
        return updCategory;
    }

    @Override
    public List<CategoryResponseDto> getAllCategories(Integer from, Integer size) {
        return repository.findAll(PageRequest.of(from / size, size)).stream()
                .map(CategoryMapper::toCategoryResponseDto).collect(Collectors.toList());
    }

    @Override
    public CategoryResponseDto getCategoryById(Long catId) {
        return CategoryMapper.toCategoryResponseDto(repository.findById(catId).orElseThrow(() -> new NotFoundException(
                String.format("Category с id: %d не найдена", catId))));
    }
}
