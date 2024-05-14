package com.project.bookstore.controller;

import static com.project.bookstore.config.SwaggerConstants.DELETE_CATEGORY_BY_ID_DESC;
import static com.project.bookstore.config.SwaggerConstants.DELETE_CATEGORY_BY_ID_SUM;
import static com.project.bookstore.config.SwaggerConstants.FIND_ALL_BOOKS_BY_CATEGORY_ID_DESC;
import static com.project.bookstore.config.SwaggerConstants.FIND_ALL_BOOKS_BY_CATEGORY_ID_SUM;
import static com.project.bookstore.config.SwaggerConstants.FIND_ALL_CATEGORIES_DESC;
import static com.project.bookstore.config.SwaggerConstants.FIND_ALL_CATEGORIES_SUM;
import static com.project.bookstore.config.SwaggerConstants.FIND_CATEGORY_BY_ID_DESC;
import static com.project.bookstore.config.SwaggerConstants.FIND_CATEGORY_BY_ID_SUM;
import static com.project.bookstore.config.SwaggerConstants.SAVE_ALL_CATEGORIES_DESC;
import static com.project.bookstore.config.SwaggerConstants.SAVE_ALL_CATEGORIES_SUM;
import static com.project.bookstore.config.SwaggerConstants.SAVE_CATEGORY_DESC;
import static com.project.bookstore.config.SwaggerConstants.SAVE_CATEGORY_SUM;
import static com.project.bookstore.config.SwaggerConstants.UPDATE_CATEGORY_BY_ID_DESC;
import static com.project.bookstore.config.SwaggerConstants.UPDATE_CATEGORY_BY_ID_SUM;

import com.project.bookstore.dto.book.BookWithoutCategoryIdsDto;
import com.project.bookstore.dto.category.CategoryDto;
import com.project.bookstore.dto.category.CreateCategoryRequestDto;
import com.project.bookstore.service.book.BookService;
import com.project.bookstore.service.category.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "Categories management endpoints")
public class CategoryController {
    private final CategoryService categoryService;
    private final BookService bookService;

    @PostMapping
    @PreAuthorize("hasAuthority('admin')")
    @Operation(summary = SAVE_CATEGORY_SUM, description = SAVE_CATEGORY_DESC)
    public CategoryDto save(@RequestBody CreateCategoryRequestDto requestDto) {
        return categoryService.save(requestDto);
    }

    @PostMapping("/all")
    @PreAuthorize("hasAuthority('admin')")
    @Operation(summary = SAVE_ALL_CATEGORIES_SUM, description = SAVE_ALL_CATEGORIES_DESC)
    public List<CategoryDto> saveAll(@RequestBody List<CreateCategoryRequestDto> requestDtos) {
        return categoryService.saveAll(requestDtos);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('user')")
    @Operation(summary = FIND_ALL_CATEGORIES_SUM, description = FIND_ALL_CATEGORIES_DESC)
    public List<CategoryDto> findAll(Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('user')")
    @Operation(summary = FIND_CATEGORY_BY_ID_SUM, description = FIND_CATEGORY_BY_ID_DESC)
    public CategoryDto findById(@PathVariable Long id) {
        return categoryService.findById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('admin')")
    @Operation(summary = UPDATE_CATEGORY_BY_ID_SUM, description = UPDATE_CATEGORY_BY_ID_DESC)
    public CategoryDto updateById(
            @PathVariable Long id,
            @RequestBody CreateCategoryRequestDto requestDto) {
        return categoryService.update(id, requestDto);
    }

    @GetMapping("/{id}/books")
    @PreAuthorize("hasAuthority('user')")
    @Operation(summary = FIND_ALL_BOOKS_BY_CATEGORY_ID_SUM,
            description = FIND_ALL_BOOKS_BY_CATEGORY_ID_DESC)
    public List<BookWithoutCategoryIdsDto> findAllBooksByCategoryId(
            @PathVariable Long id,
            Pageable pageable) {
        return bookService.findAllByCategoryId(id, pageable);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('admin')")
    @Operation(summary = DELETE_CATEGORY_BY_ID_SUM, description = DELETE_CATEGORY_BY_ID_DESC)
    public void deleteById(@PathVariable Long id) {
        categoryService.deleteById(id);
    }
}
