package com.project.bookstore.service.category;

import com.project.bookstore.dto.category.CategoryDto;
import com.project.bookstore.dto.category.CreateCategoryRequestDto;

import java.util.List;

public interface CategoryService {
    CategoryDto save(CreateCategoryRequestDto requestDto);
    List<CategoryDto> saveAll(List<CreateCategoryRequestDto> requestDtos);
    List<CategoryDto> findAll();
    CategoryDto findById(Long id);
    CategoryDto update(Long id, CreateCategoryRequestDto requestDto);
    void deleteById(Long id);
}
