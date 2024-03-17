package com.project.bookstore.service.category;

import com.project.bookstore.dto.category.CategoryDto;
import com.project.bookstore.dto.category.CreateCategoryRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    CategoryDto save(CreateCategoryRequestDto requestDto);

    List<CategoryDto> saveAll(List<CreateCategoryRequestDto> requestDtos);

    List<CategoryDto> findAll(Pageable pageable);

    CategoryDto findById(Long id);

    CategoryDto update(Long id, CreateCategoryRequestDto requestDto);

    void deleteById(Long id);
}
