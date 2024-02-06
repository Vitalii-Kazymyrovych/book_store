package com.project.bookstore.service;

import com.project.bookstore.dto.BookDto;
import com.project.bookstore.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    BookDto findById(Long id);

    List<BookDto> findAll();

    BookDto updateById(Long id, CreateBookRequestDto requestDto);

    void deleteById(Long id);
}
