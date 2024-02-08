package com.project.bookstore.service;

import com.project.bookstore.dto.BookDto;
import com.project.bookstore.dto.BookSearchParameters;
import com.project.bookstore.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> saveAll(CreateBookRequestDto[] requestDtos);

    BookDto findById(Long id);

    List<BookDto> findAll();

    List<BookDto> search(BookSearchParameters searchParameters);

    BookDto updateById(Long id, CreateBookRequestDto requestDto);

    void deleteById(Long id);
}
