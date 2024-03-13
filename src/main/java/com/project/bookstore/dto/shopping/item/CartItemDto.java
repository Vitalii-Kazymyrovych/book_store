package com.project.bookstore.dto.shopping.item;

public record CartItemDto(
        Long id,
        Long bookId,
        String bookTitle,
        int quantity) {
}
