package com.project.bookstore.service.shopping.cart;

import com.project.bookstore.dto.cart.item.CartItemDto;
import com.project.bookstore.dto.cart.item.CartItemWithoutBookTitleDto;
import com.project.bookstore.dto.cart.item.CreateCartItemRequestDto;
import com.project.bookstore.dto.cart.item.UpdateCartItemRequestDto;
import com.project.bookstore.dto.shopping.cart.ShoppingCartDto;

public interface ShoppingCartService {

    ShoppingCartDto findCartByUserName(String username);

    CartItemWithoutBookTitleDto postItem(String username, CreateCartItemRequestDto requestDto);

    CartItemDto updateItem(String username, Long id, UpdateCartItemRequestDto requestDto);

    void deleteItem(Long id, String username);
}
