package com.project.bookstore.dto.cart.item;

public record CreateCartItemRequestDto(
        Long bookId,
        Integer quantity) {
}