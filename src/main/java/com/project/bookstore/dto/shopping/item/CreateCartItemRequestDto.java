package com.project.bookstore.dto.shopping.item;

public record CreateCartItemRequestDto(
        Long bookId,
        Integer quantity) {
}
