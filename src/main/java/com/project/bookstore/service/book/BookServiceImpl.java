package com.project.bookstore.service.book;

import com.project.bookstore.dto.book.BookDto;
import com.project.bookstore.dto.book.BookSearchParameters;
import com.project.bookstore.dto.book.BookWithoutCategoryIdsDto;
import com.project.bookstore.dto.book.CreateBookRequestDto;
import com.project.bookstore.exception.EntityNotFoundException;
import com.project.bookstore.mapper.BookMapper;
import com.project.bookstore.model.Book;
import com.project.bookstore.repository.book.BookRepository;
import com.project.bookstore.repository.book.BookSpecificationBuilder;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
    public List<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public List<BookDto> search(BookSearchParameters searchParameters, Pageable pageable) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(searchParameters);
        return bookRepository.findAll(bookSpecification, pageable)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto updateById(Long id, CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Cant find book by id: " + id);
        }
        book.setId(id);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public void deleteById(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Cant find book by id: " + id);
        }
        bookRepository.deleteById(id);
    }

    @Override
    public List<BookWithoutCategoryIdsDto> findAllByCategoryId(Long id, Pageable pageable) {
        return bookRepository.findAllByCategoriesId(id, pageable)
                .stream()
                .map(bookMapper::toBookWithoutCategoryIdsDto)
                .toList();
    }
}
