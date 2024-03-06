package com.project.bookstore.controller;

import com.project.bookstore.dto.book.BookWithoutCategoryIdsDto;
import com.project.bookstore.dto.category.CategoryDto;
import com.project.bookstore.dto.category.CreateCategoryRequestDto;
import com.project.bookstore.service.book.BookService;
import com.project.bookstore.service.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final BookService bookService;

    @PreAuthorize("hasAuthority('admin')")
    @PostMapping
    public CategoryDto save(@RequestBody CreateCategoryRequestDto requestDto) {
        return categoryService.save(requestDto);
    }

    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/all")
    public List<CategoryDto> saveAll(@RequestBody List<CreateCategoryRequestDto> requestDtos) {
        return categoryService.saveAll(requestDtos);
    }

    @PreAuthorize("hasAuthority('user')")
    @GetMapping
    public List<CategoryDto> findAll() {
        return categoryService.findAll();
    }

    @PreAuthorize("hasAuthority('user')")
    @GetMapping("/{id}")
    public CategoryDto findById(@PathVariable Long id) {
        return categoryService.findById(id);
    }

    @PreAuthorize("hasAuthority('admin')")
    @PutMapping("/{id}")
    public CategoryDto updateById(
            @PathVariable Long id,
            @RequestBody CreateCategoryRequestDto requestDto) {
        return categoryService.update(id, requestDto);
    }

    @PreAuthorize("hasAuthority('user')")
    @GetMapping("/{id}/books")
    public List<BookWithoutCategoryIdsDto> findAllBooksByCategoryId(@PathVariable Long id) {
        return bookService.findAllByCategoryId(id);
    }

    @PreAuthorize("hasAuthority('admin')")
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        categoryService.deleteById(id);
    }
}
