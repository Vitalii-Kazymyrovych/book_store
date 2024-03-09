package com.project.bookstore.controller;

import com.project.bookstore.dto.cart.item.CartItemDto;
import com.project.bookstore.dto.cart.item.CartItemWithoutBookTitleDto;
import com.project.bookstore.dto.cart.item.CreateCartItemRequestDto;
import com.project.bookstore.dto.cart.item.UpdateCartItemRequestDto;
import com.project.bookstore.dto.shopping.cart.ShoppingCartDto;
import com.project.bookstore.service.shopping.cart.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PreAuthorize("hasAuthority('user')")
    @GetMapping
    public ShoppingCartDto getCart(Authentication authentication) {
        return shoppingCartService.findCartByUserName(authentication.getName());
    }

    @PreAuthorize("hasAuthority('user')")
    @PostMapping
    public CartItemWithoutBookTitleDto postItem(
            Authentication authentication,
            @RequestBody CreateCartItemRequestDto requestDto) {
        return shoppingCartService.postItem(authentication.getName(), requestDto);
    }

    @PreAuthorize("hasAuthority('user')")
    @PutMapping("/cart-items/{id}")
    public CartItemDto updateItem(
            @PathVariable Long id,
            Authentication authentication,
            @RequestBody UpdateCartItemRequestDto requestDto) {
        return shoppingCartService.updateItem(authentication.getName(), id, requestDto);
    }

    @PreAuthorize("hasAuthority('user')")
    @DeleteMapping("/cart-items/{id}")
    public void deleteItem(@PathVariable Long id, Authentication authentication) {
        shoppingCartService.deleteItem(id, authentication.getName());
    }
}
