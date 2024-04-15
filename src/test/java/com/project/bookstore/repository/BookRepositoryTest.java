package com.project.bookstore.repository;

import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;
import com.project.bookstore.model.Book;

import com.project.bookstore.repository.book.BookRepository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:database/repository/book/01-prepare-db-for-book-repository-test.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "classpath:database/repository/book/02-clear-db-after-book-repository-test.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
class BookRepositoryTest {
    private static Pageable pageable;
    @Autowired
    private BookRepository bookRepository;

    @BeforeAll
    public static void setUp() {
        Sort sort = Sort.by("id").ascending();
        pageable = PageRequest.of(0, 10, sort);
    }

    // Find all by categories id
    @Test
    @DisplayName("Find all by categories id with valid category id and valid pageable")
    public void findAllByCategoriesId_ValidCategoryIdAndPageable_BooksReturned() {
        // Given
        List<Book> books = bookRepository.findAll();
        Page<Book> expected = new PageImpl<>(books, pageable, books.size());

        // When
        Page<Book> actual = bookRepository.findAllByCategoriesId(1L, pageable);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Find all by categories id with invalid category id and valid pageable")
    public void findAllByCategoriesId_InvalidCategoryId_EmptyPageReturned() {
        // Given
        int expected = 0;

        // When
        Page<Book> actual = bookRepository.findAllByCategoriesId(2L, pageable);

        // Then
        assertTrue(actual.isEmpty());
        assertEquals(expected, actual.getTotalPages());
        assertEquals(expected, actual.getTotalElements());
    }

    @Test
    @DisplayName("Find all by categories id with null category id and valid pageable")
    public void findAllByCategoriesId_NullCategoryId_ExceptionThrown() {
        // Given
        int expected = 0;

        // When
        Page<Book> actual = bookRepository.findAllByCategoriesId(null, pageable);

        // Then
        assertTrue(actual.isEmpty());
        assertEquals(expected, actual.getTotalPages());
        assertEquals(expected, actual.getTotalElements());
    }

    @Test
    @DisplayName("Find all by categories id with valid category id and null pageable")
    public void findAllByCategoriesId_ValidCategoryIdAndNullPageable_BooksReturnedWithoutPagination() {
        // Given
        List<Book> expected = bookRepository.findAll();

        // When
        Page<Book> actual = bookRepository.findAllByCategoriesId(1L, null);

        // Then
        assertEquals(expected, actual.stream().toList());
    }
}
