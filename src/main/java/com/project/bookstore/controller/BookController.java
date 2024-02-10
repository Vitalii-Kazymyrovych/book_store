package com.project.bookstore.controller;

import com.project.bookstore.dto.BookDto;
import com.project.bookstore.dto.BookSearchParameters;
import com.project.bookstore.dto.CreateBookRequestDto;
import com.project.bookstore.service.BookService;
import java.util.List;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/books")
public class BookController {
    private final BookService bookService;

    @GetMapping
    public List<BookDto> findAll() {
        return bookService.findAll();
    }

    @GetMapping("/search")
    public List<BookDto> search(BookSearchParameters searchParameters) {
        return bookService.search(searchParameters);
    }

    @GetMapping("/{id}")
    public BookDto findById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @PostMapping
    public BookDto save(@RequestBody @Valid CreateBookRequestDto requestDto) {
        return bookService.save(requestDto);
    }

    @PostMapping("/all")
    public List<BookDto> saveAll(@RequestBody @Valid CreateBookRequestDto[] requestDtos) {
        return bookService.saveAll(requestDtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDto> updateById(@PathVariable Long id,
                                              @RequestBody @Valid CreateBookRequestDto requestDto) {
        return ResponseEntity.ok(bookService.updateById(id, requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
