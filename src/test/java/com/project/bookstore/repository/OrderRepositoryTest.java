package com.project.bookstore.repository;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;
import com.project.bookstore.model.Order;

import com.project.bookstore.repository.order.OrderRepository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:database/repository/order/01-prepare-db-for-order-repository-test.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "classpath:database/repository/order/02-clear-db-after-order-repository-test.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
class OrderRepositoryTest {
    private static Pageable pageable;
    @Autowired
    private OrderRepository orderRepository;

    @BeforeAll
    public static void setUp() {
        Sort sort = Sort.by("id").ascending();
        pageable = PageRequest.of(0, 10, sort);
    }

    // Find all with items by user email
    @Test
    @DisplayName("Find all with items by user email " +
            "with valid email and valid pageable")
    public void findAllWithItemsByUserEmail_ValidEmailAndPageable_PageReturned() {
        // Given
        String email = "randomUser123@domain.com";
        List<Order> expected = orderRepository.findAll();
        // When
        Page<Order> actual = orderRepository
                .findAllWithItemsByUserEmail(email, pageable);
        // Then
        assertEquals(expected, actual.stream().toList());
    }

    @Test
    @DisplayName("Find all with items by user email " +
            "with invalid email and valid pageable")
    public void findAllWithItemsByUserEmail_InvalidEmailValidPageable_EmptyPageReturned() {
        // Given
        String invalidEmail = "invalid email";
        int expected = 0;

        // When
        Page<Order> actual = orderRepository
                .findAllWithItemsByUserEmail(invalidEmail, pageable);

        // Then
        assertTrue(actual.isEmpty());
        assertEquals(expected, actual.getTotalPages());
        assertEquals(expected, actual.getTotalElements());
    }

    @Test
    @DisplayName("Find all with items by user email " +
            "with null email and valid pageable")
    public void findAllWithItemsByUserEmail_NullEmail_EmptyPageReturned() {
        // Given
        String nullEmail = null;
        int expected = 0;

        // When
        Page<Order> actual = orderRepository
                .findAllWithItemsByUserEmail(nullEmail, pageable);

        // Then
        assertTrue(actual.isEmpty());
        assertEquals(expected, actual.getTotalPages());
        assertEquals(expected, actual.getTotalElements());
    }

    @Test
    @DisplayName("Find all with items by user email " +
            "with valid email and null pageable")
    public void findAllWithItemsByUserEmail_ValidEmailNullPageable_ExceptionThrown() {
        // Given
        List<Order> expected = orderRepository.findAll();
        // When
        Page<Order> actual = orderRepository
                .findAllWithItemsByUserEmail("randomUser123@domain.com", null);
        // Then
        assertEquals(expected, actual.stream().toList());
    }

    // Find with items and user by id
    @Test
    @DisplayName("Find with items and user by id with valid id")
    public void findWithItemsAndUserById_ValidId_OrderOptionalReturned() {
        //Given
        Optional<Order> expected = Optional.of(orderRepository.findAll().get(0));

        //When
        Optional<Order> actual = orderRepository.findWithItemsAndUserById(1L);

        //Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Find with items and user by id with invalid id")
    public void findWithItemsAndUserById_InvalidId_EmptyOptionalReturned() {
        // Given
        Long invalidId = 3L;
        Optional<Order> expected = Optional.empty();

        // When
        Optional<Order> actual =  orderRepository.findWithItemsAndUserById(invalidId);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Find with items and user by id with null id")
    public void findWithItemsAndUserById_NullId_EmptyOptionalReturned() {
        // Given
        Long nullId = null;
        Optional<Order> expected = Optional.empty();

        // When
        Optional<Order> actual =  orderRepository.findWithItemsAndUserById(nullId);

        // Then
        assertEquals(expected, actual);
    }
}