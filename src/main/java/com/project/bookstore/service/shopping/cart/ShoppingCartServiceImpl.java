package com.project.bookstore.service.shopping.cart;

import com.project.bookstore.dto.shopping.cart.ShoppingCartDto;
import com.project.bookstore.dto.shopping.item.CartItemWithoutBookTitleDto;
import com.project.bookstore.dto.shopping.item.CreateCartItemRequestDto;
import com.project.bookstore.dto.shopping.item.UpdateCartItemRequestDto;
import com.project.bookstore.exception.AuthorizationException;
import com.project.bookstore.exception.EntityNotFoundException;
import com.project.bookstore.mapper.CartItemMapper;
import com.project.bookstore.mapper.ShoppingCartMapper;
import com.project.bookstore.model.CartItem;
import com.project.bookstore.model.ShoppingCart;
import com.project.bookstore.model.User;
import com.project.bookstore.repository.shopping.cart.ShoppingCartRepository;
import com.project.bookstore.repository.shopping.item.CartItemRepository;
import com.project.bookstore.repository.user.UserRepository;
import java.util.HashSet;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    @Override
    public ShoppingCartDto findCartByUserName(String username) {
        return shoppingCartMapper.toDto(
                shoppingCartRepository.findWithCartItemsByUserEmail(
                        username).orElseGet(
                                () -> createShoppingCart(username)));
    }

    @Override
    public CartItemWithoutBookTitleDto postItem(
            String username,
            CreateCartItemRequestDto requestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserEmail(username)
                .orElseGet(() -> createShoppingCart(username));
        CartItem newItem = cartItemMapper.toModel(requestDto);
        newItem.setShoppingCart(shoppingCart);
        CartItem savedItem = cartItemRepository.save(newItem);
        return cartItemMapper.toCartItemWithoutBookTitleDto(savedItem);
    }

    @Override
    public CartItemWithoutBookTitleDto updateItem(
            String username,
            Long id,
            UpdateCartItemRequestDto requestDto) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "Can't find cart item by id: " + id));
        authorize(username, cartItem);
        cartItem.setQuantity(requestDto.quantity());
        return cartItemMapper.toCartItemWithoutBookTitleDto(cartItemRepository.save(cartItem));
    }

    @Override
    public void deleteItem(Long id, String username) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "Can't find cart item by id: " + id));
        authorize(username, cartItem);
        cartItemRepository.deleteById(id);
    }

    @Override
    public void clearShoppingCart(ShoppingCart shoppingCart) {
        shoppingCart.getCartItems().forEach(cartItemRepository::delete);
        shoppingCart.setCartItems(new HashSet<>());
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart createShoppingCart(String username) {
        User currentUser = userRepository.findByEmail(username)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "Can't find user by username: "
                                        + username));
        return shoppingCartRepository.save(new ShoppingCart(currentUser));
    }

    private void authorize(String username, CartItem cartItem) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserEmail(
                username).orElseGet(
                        () -> createShoppingCart(username));
        if (!Objects.equals(cartItem.getShoppingCart().getId(), shoppingCart.getId())) {
            throw new AuthorizationException(
                    "Access denied. You can't edit shopping cart of another user.");
        }
    }
}
