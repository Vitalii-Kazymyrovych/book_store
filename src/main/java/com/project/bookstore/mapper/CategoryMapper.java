package com.project.bookstore.mapper;

import com.project.bookstore.config.MapperConfig;
import com.project.bookstore.dto.category.CategoryDto;
import com.project.bookstore.dto.category.CreateCategoryRequestDto;
import com.project.bookstore.model.Category;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toModel(CreateCategoryRequestDto requestDto);
}
