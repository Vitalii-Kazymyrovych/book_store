package com.project.bookstore.dto.shopping.item;

public record CartItemWithoutBookTitleDto(
        Long id,
        Long bookId,
        int quantity) {
}
