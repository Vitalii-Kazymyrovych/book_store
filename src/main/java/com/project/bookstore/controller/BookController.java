package com.project.bookstore.controller;

import com.project.bookstore.config.SwaggerConstants;
import com.project.bookstore.dto.book.BookDto;
import com.project.bookstore.dto.book.BookSearchParameters;
import com.project.bookstore.dto.book.CreateBookRequestDto;
import com.project.bookstore.service.book.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Books management endpoints")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/books")
public class BookController {
    private final BookService bookService;

    @GetMapping
    @PreAuthorize("hasAuthority('user')")
    @Operation(summary = SwaggerConstants.FIND_ALL_BOOKS_SUM,
            description = SwaggerConstants.FIND_ALL_BOOKS_DESC)
    public List<BookDto> findAll(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('user')")
    @Operation(summary = SwaggerConstants.SEARCH_BOOKS_SUM,
            description = SwaggerConstants.SEARCH_BOOKS_DESC)
    public List<BookDto> search(BookSearchParameters searchParameters, Pageable pageable) {
        return bookService.search(searchParameters, pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('user')")
    @Operation(summary = SwaggerConstants.FIND_BOOK_BY_ID_SUM,
            description = SwaggerConstants.FIND_BOOK_BY_ID_DESC)
    public BookDto findById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('admin')")
    @Operation(summary = SwaggerConstants.SAVE_BOOK_SUM,
            description = SwaggerConstants.SAVE_BOOK_DESC)
    public BookDto save(@RequestBody @Valid CreateBookRequestDto requestDto) {
        return bookService.save(requestDto);
    }

    @PostMapping("/all")
    @PreAuthorize("hasAuthority('admin')")
    @Operation(summary = SwaggerConstants.SAVE_ALL_BOOKS_SUM,
            description = SwaggerConstants.SAVE_ALL_BOOKS_DESC)
    public List<BookDto> saveAll(@RequestBody @Valid CreateBookRequestDto[] requestDtos) {
        return bookService.saveAll(requestDtos);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('admin')")
    @Operation(summary = SwaggerConstants.UPDATE_BOOK_BY_ID_SUM,
            description = SwaggerConstants.UPDATE_BOOK_BY_ID_DESC)
    public BookDto updateById(
            @PathVariable Long id,
            @RequestBody @Valid CreateBookRequestDto requestDto) {
        return bookService.updateById(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('admin')")
    @Operation(summary = SwaggerConstants.DELETE_BOOK_BY_ID_SUM,
            description = SwaggerConstants.DELETE_BOOK_BY_ID_DESC)
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
