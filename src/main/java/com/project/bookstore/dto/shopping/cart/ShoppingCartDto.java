package com.project.bookstore.dto.shopping.cart;

import com.project.bookstore.dto.shopping.item.CartItemDto;
import java.util.List;

public record ShoppingCartDto(
        Long id,
        Long userId,
        List<CartItemDto> cartItems) {
}
