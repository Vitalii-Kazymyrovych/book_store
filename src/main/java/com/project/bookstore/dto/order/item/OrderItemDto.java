package com.project.bookstore.dto.order.item;

public record OrderItemDto(
        Long id,
        Long bookId,
        int quantity) {
}
