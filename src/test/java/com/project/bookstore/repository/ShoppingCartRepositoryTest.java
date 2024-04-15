package com.project.bookstore.repository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import com.project.bookstore.model.CartItem;
import com.project.bookstore.model.ShoppingCart;
import com.project.bookstore.repository.shopping.cart.ShoppingCartRepository;
import com.project.bookstore.repository.shopping.item.CartItemRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:database/repository/shopping-cart/01-prepare-db-for-shopping-cart-repository-test.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "classpath:database/repository/shopping-cart/02-clear-db-after-shopping-cart-repository-test.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
class ShoppingCartRepositoryTest {
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;


    // Find by user email
    @Test
    @DisplayName("Find by user email with valid email")
    public void findByUserEmail_WithValidEmail_ShouldReturnShoppingCart() {
        // Given
        String validEmail = "uniqueUser456@sample.net";
        Optional<ShoppingCart> expected = shoppingCartRepository.findById(1L);
        // When
        Optional<ShoppingCart> actual = shoppingCartRepository.findByUserEmail(validEmail);
        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Find by user email with invalid email")
    public void findByUserEmail_WithInvalidEmail_ShouldReturnEmpty() {
        // Given
        String invalidEmail = "invalid email";
        Optional<ShoppingCart> expected = Optional.empty();

        // When
        Optional<ShoppingCart> actual = shoppingCartRepository.findByUserEmail(invalidEmail);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Find by user email with null email")
    public void findByUserEmail_WithNullEmail_ShouldThrowIllegalArgumentException() {
        // Given
        String nullEmail = null;
        Optional<ShoppingCart> expected = Optional.empty();

        // When
        Optional<ShoppingCart> actual = shoppingCartRepository.findByUserEmail(nullEmail);

        // Then
        assertEquals(expected, actual);
    }

    // Find with cart items by user id
    @Test
    @DisplayName("Find with cart items by user email with valid email")
    public void findWithCartItemsByUserEmail_WithValidEmail_ShouldReturnShoppingCartWithItems() {
        // Given
        String validEmail = "uniqueUser456@sample.net";
        Set<CartItem> expected = new HashSet<>(cartItemRepository.findAll());

        // When
        Set<CartItem> actual = shoppingCartRepository
                .findWithCartItemsByUserEmail(validEmail)
                .get()
                .getCartItems();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Find with cart items by user email with invalid email")
    public void findWithCartItemsByUserEmail_WithInvalidEmail_ShouldReturnEmpty() {
        // Given
        String invalidEmail = "invalid email";
        Optional<ShoppingCart> expected = Optional.empty();

        // When
        Optional<ShoppingCart> actual = shoppingCartRepository
                .findWithCartItemsByUserEmail(invalidEmail);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Find with cart items by user email with null email")
    public void findWithCartItemsByUserEmail_WithNullEmail_ShouldThrowIllegalArgumentException() {
        // Given
        String nullEmail = null;
        Optional<ShoppingCart> expected = Optional.empty();

        // When
        Optional<ShoppingCart> actual = shoppingCartRepository
                .findWithCartItemsByUserEmail(nullEmail);

        // Then
        assertEquals(expected, actual);
    }
}