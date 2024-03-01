package com.project.bookstore.service;

import com.project.bookstore.dto.book.BookDto;
import com.project.bookstore.dto.book.BookSearchParameters;
import com.project.bookstore.dto.book.CreateBookRequestDto;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(@Valid CreateBookRequestDto requestDto);

    List<BookDto> saveAll(CreateBookRequestDto[] requestDtos);

    BookDto findById(Long id);

    List<BookDto> findAll(Pageable pageable);

    List<BookDto> search(BookSearchParameters searchParameters, Pageable pageable);

    BookDto updateById(Long id, CreateBookRequestDto requestDto);

    void deleteById(Long id);
}
