package com.project.bookstore.dto.cart.item;

public record CartItemWithoutBookTitleDto(
        Long id,
        Long bookId,
        int quantity) {
}
