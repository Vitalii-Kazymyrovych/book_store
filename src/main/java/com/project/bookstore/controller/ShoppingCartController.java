package com.project.bookstore.controller;

import static com.project.bookstore.config.SwaggerConstants.DELETE_CART_ITEM_DESC;
import static com.project.bookstore.config.SwaggerConstants.DELETE_CART_ITEM_SUM;
import static com.project.bookstore.config.SwaggerConstants.GET_CART_DESC;
import static com.project.bookstore.config.SwaggerConstants.GET_CART_SUM;
import static com.project.bookstore.config.SwaggerConstants.POST_CART_ITEM_DESC;
import static com.project.bookstore.config.SwaggerConstants.POST_CART_ITEM_SUM;
import static com.project.bookstore.config.SwaggerConstants.UPDATE_CART_ITEM_DESC;
import static com.project.bookstore.config.SwaggerConstants.UPDATE_CART_ITEM_SUM;

import com.project.bookstore.dto.shopping.cart.ShoppingCartDto;
import com.project.bookstore.dto.shopping.item.CartItemWithoutBookTitleDto;
import com.project.bookstore.dto.shopping.item.CreateCartItemRequestDto;
import com.project.bookstore.dto.shopping.item.UpdateCartItemRequestDto;
import com.project.bookstore.service.shopping.cart.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Shopping carts management endpoints")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @PreAuthorize("hasAuthority('user')")
    @Operation(summary = GET_CART_SUM, description = GET_CART_DESC)
    public ShoppingCartDto getCart(Authentication authentication) {
        return shoppingCartService.findCartByUserName(authentication.getName());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('user')")
    @Operation(summary = POST_CART_ITEM_SUM, description = POST_CART_ITEM_DESC)
    public CartItemWithoutBookTitleDto postItem(
            Authentication authentication,
            @RequestBody CreateCartItemRequestDto requestDto) {
        return shoppingCartService.postItem(authentication.getName(), requestDto);
    }

    @PutMapping("/cart-items/{id}")
    @PreAuthorize("hasAuthority('user')")
    @Operation(summary = UPDATE_CART_ITEM_SUM, description = UPDATE_CART_ITEM_DESC)
    public CartItemWithoutBookTitleDto updateItem(
            @PathVariable Long id,
            Authentication authentication,
            @RequestBody UpdateCartItemRequestDto requestDto) {
        return shoppingCartService.updateItem(authentication.getName(), id, requestDto);
    }

    @DeleteMapping("/cart-items/{id}")
    @PreAuthorize("hasAuthority('user')")
    @Operation(summary = DELETE_CART_ITEM_SUM, description = DELETE_CART_ITEM_DESC)
    public void deleteItem(@PathVariable Long id, Authentication authentication) {
        shoppingCartService.deleteItem(id, authentication.getName());
    }
}
