package com.project.bookstore.service.category;

import com.project.bookstore.dto.category.CategoryDto;
import com.project.bookstore.dto.category.CreateCategoryRequestDto;
import com.project.bookstore.mapper.CategoryMapper;
import com.project.bookstore.model.Book;
import com.project.bookstore.model.Category;
import com.project.bookstore.repository.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto save(CreateCategoryRequestDto requestDto) {
        return categoryMapper.toDto(
                categoryRepository.save(
                        categoryMapper.toModel(requestDto)));
    }

    @Override
    public List<CategoryDto> saveAll(List<CreateCategoryRequestDto> requestDtos) {
        return categoryRepository.saveAll(requestDtos.stream()
                .map(categoryMapper::toModel)
                .toList())
                .stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public List<CategoryDto> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryDto findById(Long id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::toDto)
                .orElseThrow();
    }

    @Override
    public CategoryDto update(Long id, CreateCategoryRequestDto requestDto) {
        Category newCategory = categoryMapper.toModel(requestDto);
        newCategory.setId(id);
        return categoryMapper.toDto(categoryRepository.save(newCategory));
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}