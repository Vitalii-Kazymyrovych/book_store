package com.project.bookstore.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import com.project.bookstore.dto.book.BookDto;
import com.project.bookstore.dto.book.BookSearchParameters;
import com.project.bookstore.dto.book.BookWithoutCategoryIdsDto;
import com.project.bookstore.dto.book.CreateBookRequestDto;
import com.project.bookstore.exception.EntityNotFoundException;
import com.project.bookstore.mapper.BookMapper;
import com.project.bookstore.model.Book;
import com.project.bookstore.repository.book.BookRepository;
import com.project.bookstore.repository.book.BookSpecificationBuilder;
import com.project.bookstore.service.book.BookServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;
    @InjectMocks
    private BookServiceImpl bookService;

    // Test cases for save method
    @Test
    @DisplayName("Save with valid request dto")
    public void save_ValidRequestDto_ReturnsBookDto() {
        //Given
        Book book = mock(Book.class);
        BookDto expected = mock(BookDto.class);
        CreateBookRequestDto requestDto = mock(CreateBookRequestDto.class);
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookMapper.toDto(any(Book.class))).thenReturn(expected);
        when(bookMapper.toModel(requestDto)).thenReturn(book);

        //When
        BookDto actual = bookService.save(requestDto);

        //Then
        assertEquals(expected, actual);
    }

    // Test cases for saveAll method
    @Test
    @DisplayName("Save all with valid request dtos")
    public void saveAll_ValidRequestDtos_ReturnsListOfBookDtos() {
        // Given
        CreateBookRequestDto[] requestDtos = {
                mock(CreateBookRequestDto.class),
                mock(CreateBookRequestDto.class)};
        Book book = mock(Book.class);
        List<BookDto> expected = List.of(mock(BookDto.class));
        when(bookRepository.saveAll(anyList())).thenReturn(List.of(book));
        when(bookMapper.toDto(any(Book.class))).thenReturn(expected.get(0));

        // When
        List<BookDto> actual = bookService.saveAll(requestDtos);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Save all wiht empty array")
    public void saveAll_EmptyRequestDtos_ReturnsEmptyList() {
        // Given
        CreateBookRequestDto[] requestDtos = {};
        when(bookRepository.saveAll(anyList())).thenReturn(Collections.emptyList());

        // When
        List<BookDto> actual = bookService.saveAll(requestDtos);

        // Then
        assertTrue(actual.isEmpty());
    }

    @Test
    @DisplayName("Save all with null array")
    public void saveAll_NullRequestDtos_ThrowsNullPointerException() {
        // Given
        CreateBookRequestDto[] requestDtos = null;
        String expected = "Cannot read the array length because \"array\" is null";

        // When
        Exception actual = assertThrows(NullPointerException.class,
                () -> bookService.saveAll(requestDtos));

        // Then
        assertEquals(expected, actual.getMessage());
    }

    // Test cases for findById method
    @Test
    @DisplayName("Find by existing id")
    public void findById_ExistingId_ReturnsBookDto() {
        Book book = mock(Book.class);
        BookDto expected = mock(BookDto.class);
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(expected);

        // When
        BookDto actual = bookService.findById(anyLong());

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Find by non existing id")
    public void findById_NonExistingId_ThrowsEntityNotFoundException() {
        // Given
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());
        String expected = "Cant find book by id: 0";

        // When
        Exception actual = assertThrows(EntityNotFoundException.class,
                () -> bookService.findById(anyLong()));

        // Then
        assertEquals(expected, actual.getMessage());
    }

    // Test cases for findAll method
    @Test
    @DisplayName("Find all with valid pageable")
    public void findAll_ValidPageable_ReturnsPaginatedBookDtos() {
        // Given
        int expected = 2;
        Pageable pageable = mock(Pageable.class);
        Page<Book> page = mock(Page.class);
        Book[] books = {mock(Book.class), mock(Book.class)};
        when(bookRepository.findAll(pageable)).thenReturn(page);
        when(page.stream()).thenReturn(Arrays.stream(books));
        when(bookMapper.toDto(any(Book.class)))
                .thenReturn(mock(BookDto.class));

        // When
        List<BookDto> actual = bookService.findAll(pageable);

        // Then
        assertEquals(expected, actual.size());
    }

    @Test
    @DisplayName("Find all with null pageable")
    public void findAll_NullPageable_ThrowsNullPointerException() {
        // Given
        Pageable nullPageable = null;
        String expected = "Cannot invoke \"org.springframework."
                + "data.domain.Page.stream()\" because the return "
                + "value of \"com.project.bookstore.repository.book."
                + "BookRepository.findAll(org.springframework.data."
                + "domain.Pageable)\" is null";

        // When
        Exception actual = assertThrows(NullPointerException.class,
                () -> bookService.findAll(nullPageable));

        // Then
        assertEquals(expected, actual.getMessage());
    }

    // Test cases for search method
    @Test
    @DisplayName("Search with valid search parameters and pageable")
    public void search_ValidSearchParametersAndPageable_ReturnsFilteredBookDtos() {
        int expected = 2;
        BookSearchParameters searchParameters = mock(BookSearchParameters.class);
        Pageable pageable = mock(Pageable.class);
        Specification<Book> bookSpecification = mock(Specification.class);
        Page<Book> page = mock(Page.class);
        Book[] books = {mock(Book.class), mock(Book.class)};
        when(bookSpecificationBuilder.build(searchParameters)).thenReturn(bookSpecification);
        when(bookRepository.findAll(bookSpecification, pageable)).thenReturn(page);
        when(page.stream()).thenReturn(Arrays.stream(books));
        when(bookMapper.toDto(any(Book.class))).thenReturn(mock(BookDto.class));

        // When
        List<BookDto> actual = bookService.search(searchParameters, pageable);

        // Then
        assertEquals(expected, actual.size());
    }

    @Test
    @DisplayName("Search with null search parameters")
    public void search_NullSearchParameters_ThrowsNullPointerException() {
        // Given
        BookSearchParameters searchParameters = null;
        String expected = "Cannot invoke \"org.springframework.data."
                + "domain.Page.stream()\" because the return value of "
                + "\"com.project.bookstore.repository.book.BookRepository."
                + "findAll(org.springframework.data.jpa.domain.Specification, "
                + "org.springframework.data.domain.Pageable)\" is null";

        // When
        Exception actual = assertThrows(NullPointerException.class,
                () -> bookService.search(searchParameters, mock(Pageable.class)));

        // Then
        assertEquals(expected, actual.getMessage());
    }

    @Test
    @DisplayName("Search with null pageable")
    public void search_NullPageable_ThrowsNullPointerException() {
        // Given
        Pageable pageable = null;
        String expected = "Cannot invoke \"org.springframework"
                + ".data.domain.Page.stream()\" because the "
                + "return value of \"com.project.bookstore."
                + "repository.book.BookRepository.findAll(org."
                + "springframework.data.jpa.domain.Specification, "
                + "org.springframework.data.domain.Pageable)\" is null";
        // When
        Exception actual = assertThrows(NullPointerException.class,
                () -> bookService.search(mock(BookSearchParameters.class), pageable));

        // Then
        assertEquals(expected, actual.getMessage());
    }

    // Test cases for updateById method
    @Test
    @DisplayName("Update by id with valid id and valid requestDto")
    public void updateById_ValidIdAndRequestDto_ReturnsUpdatedBookDto() {
        // Given
        CreateBookRequestDto requestDto = mock(CreateBookRequestDto.class);
        Book book = mock(Book.class);
        BookDto expected = mock(BookDto.class);
        when(bookRepository.existsById(anyLong())).thenReturn(true);
        when(bookMapper.toModel(requestDto)).thenReturn(book);
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookMapper.toDto(any(Book.class))).thenReturn(expected);
        // When
        BookDto actual = bookService.updateById(anyLong(), requestDto);
        // Then
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Update by id with null requestDto")
    public void updateById_NullRequestDto_ThrowsNullPointerException() {
        // Given
        CreateBookRequestDto requestDto = null;
        // When & Then
        Exception actual = assertThrows(EntityNotFoundException.class,
                () -> bookService.updateById(anyLong(), requestDto));
    }

    // Test cases for deleteById method
    @Test
    @DisplayName("Delete by id with valid id")
    public void deleteById_ExistingId_DeletesBook() {
        // Given
        Long id = 1L;
        when(bookRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(bookRepository).deleteById(id);
        // When
        bookService.deleteById(id);
        // Then
        verify(bookRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Delete by id with non existing id")
    public void deleteById_NonExistingId_ThrowsEntityNotFoundException() {
        // Given
        Long id = 2L;
        // When & Then
        Exception actual = assertThrows(EntityNotFoundException.class,
                () -> bookService.deleteById(id));
    }

    // Test cases for findAllByCategoryId method
    @Test
    @DisplayName("Find all by category id with valid id")
    public void findAllByCategoryId_ExistingCategoryId_ReturnsBookWithoutCategoryIdsDtos() {
        // Given
        Long categoryId = 1L;
        Pageable pageable = mock(Pageable.class);
        Page<Book> page = mock(Page.class);
        Book book = mock(Book.class);
        Book[] books = {book, book};
        BookWithoutCategoryIdsDto dto = mock(BookWithoutCategoryIdsDto.class);
        List<BookWithoutCategoryIdsDto> expected = List.of(dto, dto);
        when(bookRepository.findAllByCategoriesId(categoryId, pageable)).thenReturn(page);
        when(page.stream()).thenReturn(Arrays.stream(books));
        when(bookMapper.toBookWithoutCategoryIdsDto(any(Book.class)))
                .thenReturn(dto);

        // When
        List<BookWithoutCategoryIdsDto> actual = bookService.findAllByCategoryId(categoryId, pageable);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Find all by category id with non existing id")
    public void findAllByCategoryId_NonExistingCategoryId_ReturnsEmptyList() {
        // Given
        Long categoryId = 2L;
        Pageable pageable = mock(Pageable.class);
        Page<Book> page = mock(Page.class);
        when(bookRepository.findAllByCategoriesId(categoryId, pageable)).thenReturn(page);

        // When
        List<BookWithoutCategoryIdsDto> actual = bookService.findAllByCategoryId(categoryId, pageable);

        // Then
        assertTrue(actual.isEmpty());
    }

    @Test
    @DisplayName("Find all by category id with null pageable")
    public void findAllByCategoryId_NullPageable_ThrowsNullPointerException() {
        // Given
        Long categoryId = 1L;
        Pageable pageable = null;
        String expected = "Cannot invoke \"org.springframework.data.domain."
                + "Page.stream()\" because the return value of \"com.project"
                + ".bookstore.repository.book.BookRepository.findAllByCategoriesId"
                + "(java.lang.Long, org.springframework.data.domain.Pageable)\" is null";

        // When
        Exception actual = assertThrows(NullPointerException.class,
                () -> bookService.findAllByCategoryId(categoryId, pageable));

        // Then
        assertEquals(expected, actual.getMessage());
    }
}