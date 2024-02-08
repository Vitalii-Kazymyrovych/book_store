package com.project.bookstore.service;

import com.project.bookstore.dto.BookDto;
import com.project.bookstore.dto.BookSearchParameters;
import com.project.bookstore.dto.CreateBookRequestDto;
import com.project.bookstore.exception.EntityNotFoundException;
import com.project.bookstore.mapper.BookMapper;
import com.project.bookstore.model.Book;
import com.project.bookstore.repository.SpecificationBuilder;
import com.project.bookstore.repository.book.BookRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.project.bookstore.repository.book.BookSpecificationBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        return bookMapper.toDto(bookRepository
                .save(bookMapper.toModel(requestDto)));
    }

    @Override
    public List<BookDto> saveAll(CreateBookRequestDto[] requestDtos) {
        return bookRepository.saveAll(Arrays.stream(requestDtos)
                .map(bookMapper::toModel)
                .toList())
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto findById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Cant find book by id: " + id));
        return bookMapper.toDto(book);
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public List<BookDto> search(BookSearchParameters searchParameters) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(searchParameters);
        return bookRepository.findAll(bookSpecification)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto updateById(Long id, CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        book.setId(id);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }
}
