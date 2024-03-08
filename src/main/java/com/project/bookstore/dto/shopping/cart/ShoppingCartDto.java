package com.project.bookstore.dto.shopping.cart;

import com.project.bookstore.dto.cart.item.CartItemDto;
import com.project.bookstore.model.CartItem;

import java.util.List;

public record ShoppingCartDto(
        Long id,
        Long userId,
        List<CartItemDto> cartItems) {
}
