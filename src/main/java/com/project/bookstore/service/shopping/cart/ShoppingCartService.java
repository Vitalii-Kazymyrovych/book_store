package com.project.bookstore.service.shopping.cart;

import com.project.bookstore.dto.shopping.cart.ShoppingCartDto;
import com.project.bookstore.dto.shopping.item.CartItemWithoutBookTitleDto;
import com.project.bookstore.dto.shopping.item.CreateCartItemRequestDto;
import com.project.bookstore.dto.shopping.item.UpdateCartItemRequestDto;
import com.project.bookstore.model.ShoppingCart;

public interface ShoppingCartService {

    ShoppingCartDto findCartByUserName(String username);

    CartItemWithoutBookTitleDto postItem(
            String username,
            CreateCartItemRequestDto requestDto);

    CartItemWithoutBookTitleDto updateItem(
            String username,
            Long id,
            UpdateCartItemRequestDto requestDto);

    ShoppingCart createShoppingCart(String username);

    void deleteItem(Long id, String username);

    void clearShoppingCart(ShoppingCart shoppingCart);
}
