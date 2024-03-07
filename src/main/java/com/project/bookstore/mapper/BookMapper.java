package com.project.bookstore.mapper;

import com.project.bookstore.config.MapperConfig;
import com.project.bookstore.dto.book.BookDto;
import com.project.bookstore.dto.book.BookWithoutCategoryIdsDto;
import com.project.bookstore.dto.book.CreateBookRequestDto;
import com.project.bookstore.model.Book;
import com.project.bookstore.model.Category;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface BookMapper {

    @Mapping(
            target = "categoryIds",
            source = "categories",
            qualifiedByName = "createCategoryIdsSet")
    BookDto toDto(Book book);

    BookWithoutCategoryIdsDto toBookWithoutCategoryIdsDto(Book book);

    @Mapping(
            target = "categories",
            source = "categoryIds",
            qualifiedByName = "createCategoriesSet")
    Book toModel(CreateBookRequestDto requestDto);

    @Named("createCategoryIdsSet")
    default Set<Long> createCategoryIdsSet(Set<Category> categories) {
        return categories
                .stream()
                .map(Category::getId)
                .collect(Collectors.toSet());
    }

    @Named("createCategoriesSet")
    default Set<Category> createCategoriesSet(List<Long> categoryIds) {
        return categoryIds
                .stream()
                .map(Category::new)
                .collect(Collectors.toSet());
    }
}
