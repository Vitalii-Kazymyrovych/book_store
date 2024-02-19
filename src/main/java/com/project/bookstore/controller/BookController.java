package com.project.bookstore.controller;

import com.project.bookstore.dto.book.BookDto;
import com.project.bookstore.dto.book.BookSearchParameters;
import com.project.bookstore.dto.book.CreateBookRequestDto;
import com.project.bookstore.service.BookService;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Book manager",
        description = "Endpoints for managing books")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/books")
public class BookController {
    private final BookService bookService;

    @GetMapping
    @Operation(summary = "Find all books",
            description = "Get list of available books")
    public List<BookDto> findAll(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @GetMapping("/search")
    @Operation(summary = "Search books by certain parameters",
            description = "Get list of books found by title, author, price or isbn")
    public List<BookDto> search(BookSearchParameters searchParameters, Pageable pageable) {
        return bookService.search(searchParameters, pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find book by id",
            description = "Find book by id")
    public BookDto findById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Save new book to database",
            description = "Save new book to database")
    public BookDto save(@RequestBody @Valid CreateBookRequestDto requestDto) {
        return bookService.save(requestDto);
    }

    @PostMapping("/all")
    @Operation(summary = "Save list of books to database",
            description = "Save list of books to database")
    public List<BookDto> saveAll(@RequestBody @Valid CreateBookRequestDto[] requestDtos) {
        return bookService.saveAll(requestDtos);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update book by id",
            description = "Update book by id")
    public ResponseEntity<BookDto> updateById(@PathVariable Long id,
                                              @RequestBody @Valid CreateBookRequestDto requestDto) {
        return ResponseEntity.ok(bookService.updateById(id, requestDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete book by id",
            description = "Delete book by id")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
