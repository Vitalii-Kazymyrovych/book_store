package com.project.bookstore.service;

import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
import com.project.bookstore.service.shopping.cart.ShoppingCartServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceImplTest {
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private ShoppingCartMapper shoppingCartMapper;
    @Mock
    private CartItemMapper cartItemMapper;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    // Find cart by username
    @Test
    @DisplayName("Find cart by username with existing user")
    public void findCartByUserName_UserExists_ShoppingCartDtoReturned() {
        // Given
        String username = "existingUser@example.com";
        ShoppingCart existingCart = mock(ShoppingCart.class);
        ShoppingCartDto expected = mock(ShoppingCartDto.class);
        when(shoppingCartRepository.findWithCartItemsByUserEmail(username))
                .thenReturn(Optional.of(existingCart));
        when(shoppingCartMapper.toDto(existingCart)).thenReturn(expected);

        // When
        ShoppingCartDto actual = shoppingCartService.findCartByUserName(username);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Find cart by username with non-existing user")
    public void findCartByUserName_UserDoesNotExist_ThrowsEntityNotFoundException() {
        // Given
        String username = "newUser@example.com";
        String expected = "Can't find user by username: newUser@example.com";

        // When
        Exception actual = assertThrows(EntityNotFoundException.class,
                () ->shoppingCartService.findCartByUserName(username));

        // Then
        assertEquals(expected, actual.getMessage());
    }

    // Post item
    @Test
    @DisplayName("Post item by username with existing user and valid request dto")
    public void postItem_UserExistsAndValidRequest_CartItemWithoutBookTitleDtoReturned() {
        // Given
        CreateCartItemRequestDto requestDto = mock(CreateCartItemRequestDto.class);
        ShoppingCart existingCart = mock(ShoppingCart.class);
        CartItem newItem = mock(CartItem.class);
        CartItemWithoutBookTitleDto expected = mock(CartItemWithoutBookTitleDto.class);
        when(shoppingCartRepository.findByUserEmail(anyString()))
                .thenReturn(Optional.of(existingCart));
        when(cartItemMapper.toModel(requestDto)).thenReturn(newItem);
        when(cartItemRepository.save(newItem)).thenReturn(newItem);
        when(cartItemMapper.toCartItemWithoutBookTitleDto(newItem))
                .thenReturn(expected);

        // When
        CartItemWithoutBookTitleDto actual = shoppingCartService
                .postItem(anyString(), requestDto);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Post item by username with non-existing user and valid request dto")
    public void postItem_UserDoesNotExist_ThrowsEntityNotFoundException() {
        // Given
        String username = "newUser@example.com";
        String expected = "Can't find user by username: newUser@example.com";

        // When
        Exception actual = assertThrows(EntityNotFoundException.class,
                () ->shoppingCartService.postItem(username, mock(CreateCartItemRequestDto.class)));

        // Then
        assertEquals(expected, actual.getMessage());
    }

    @Test
    @DisplayName("Post item by username with existing user and null request dto")
    public void postItem_NullRequestDto_ThrowsNullPointerException() {
        // Given
        CreateCartItemRequestDto requestDto = null;
        String expected = "Cannot invoke \"com.project.bookstore."
                + "model.CartItem.setShoppingCart(com.project."
                + "bookstore.model.ShoppingCart)\" because \""
                + "newItem\" is null";
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(mock(User.class)));

        // When
        Exception actual = assertThrows(NullPointerException.class,
                () ->shoppingCartService.postItem(anyString(), requestDto));

        // Then
        assertEquals(expected, actual.getMessage());
    }

    // Update item
    @Test
    @DisplayName("Update item with existing cart item and authorized user")
    public void updateItem_CartItemExistsAndUserAuthorized_CartItemUpdated() {
        // Given
        String username = "authorizedUser@example.com";
        Long id = 1L;
        int quantity = 10;
        UpdateCartItemRequestDto requestDto
                = mock(UpdateCartItemRequestDto.class);
        CartItem cartItem = mock(CartItem.class);
        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        CartItemWithoutBookTitleDto expected
                = mock(CartItemWithoutBookTitleDto.class);
        when(cartItemRepository.findById(anyLong()))
                .thenReturn(Optional.of(cartItem));
        when(shoppingCartRepository.findByUserEmail(username))
                .thenReturn(Optional.of(shoppingCart));
        when(cartItem.getShoppingCart()).thenReturn(shoppingCart);
        when(shoppingCart.getId()).thenReturn(id);
        when(requestDto.quantity()).thenReturn(quantity);
        when(cartItemRepository.save(cartItem)).thenReturn(cartItem);
        when(cartItemMapper.toCartItemWithoutBookTitleDto(cartItem))
                .thenReturn(expected);

        // When
        CartItemWithoutBookTitleDto actual = shoppingCartService.updateItem(username, id, requestDto);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Update item with non-existing cart item and authorized user")
    public void updateItem_CartItemDoesNotExist_EntityNotFoundExceptionThrown() {
        // Given
        String username = "user@example.com";
        Long id = 2L;
        String expected = "Can't find cart item by id: " + id;
        when(cartItemRepository.findById(id)).thenReturn(Optional.empty());

        // When
        EntityNotFoundException actual = assertThrows(
                EntityNotFoundException.class,
                () -> shoppingCartService.updateItem(username, id, mock(UpdateCartItemRequestDto.class))
        );

        // Then
        assertEquals(expected, actual.getMessage());
    }

    @Test
    @DisplayName("Update item with existing cart item and unauthorized user")
    public void updateItem_UserNotAuthorized_AuthorizationExceptionThrown() {
        // Given
        String username = "unauthorizedUser@example.com";
        Long id = 3L;
        String expected = "Access denied. You can't edit "
                + "shopping cart of another user.";
        UpdateCartItemRequestDto requestDto
                = mock(UpdateCartItemRequestDto.class);
        CartItem cartItem = mock(CartItem.class);
        ShoppingCart userShoppingCart = mock(ShoppingCart.class);
        ShoppingCart cartItemShoppingCart = mock(ShoppingCart.class);
        when(cartItemRepository.findById(id)).thenReturn(Optional.of(cartItem));
        when(shoppingCartRepository.findByUserEmail(username))
                .thenReturn(Optional.of(userShoppingCart));
        when(cartItem.getShoppingCart()).thenReturn(cartItemShoppingCart);
        when(cartItemShoppingCart.getId()).thenReturn(4L);
        when(userShoppingCart.getId()).thenReturn(id);

        // When
        AuthorizationException actual = assertThrows(
                AuthorizationException.class,
                () -> shoppingCartService.updateItem(username, id, requestDto)
        );

        // Then
        assertEquals(expected, actual.getMessage());
    }

    // Delete item
    @Test
    @DisplayName("Delete item with existing cart item and authorized user")
    public void deleteItem_CartItemExistsAndUserAuthorized_CartItemDeleted() {
        // Given
        Long id = 1L;
        String username = "authorizedUser@example.com";
        CartItem cartItem = mock(CartItem.class);
        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        when(cartItemRepository.findById(id)).thenReturn(Optional.of(cartItem));
        when(shoppingCartRepository.findByUserEmail(username)).thenReturn(Optional.of(shoppingCart));
        when(cartItem.getShoppingCart()).thenReturn(shoppingCart);
        when(shoppingCart.getId()).thenReturn(id);

        // When
        shoppingCartService.deleteItem(id, username);

        // Then
        verify(cartItemRepository).deleteById(id);
    }

    @Test
    @DisplayName("Delete item with non-existing cart item and authorized user")
    public void deleteItem_CartItemDoesNotExist_EntityNotFoundExceptionThrown() {
        // Given
        Long id = 2L;
        String username = "user@example.com";
        when(cartItemRepository.findById(id)).thenReturn(Optional.empty());
        String expected = "Can't find cart item by id: " + id;

        // When
        Exception actual = assertThrows(
                EntityNotFoundException.class,
                () -> shoppingCartService.deleteItem(id, username)
        );

        // Then
        assertEquals(expected, actual.getMessage());
    }

    @Test
    @DisplayName("Delete item with existing cart item and unauthorized user")
    public void deleteItem_UserNotAuthorized_AuthorizationExceptionThrown() {
        // Given
        Long id = 3L;
        String username = "unauthorizedUser@example.com";
        CartItem cartItem = mock(CartItem.class);
        ShoppingCart userShoppingCart = mock(ShoppingCart.class);
        ShoppingCart cartItemShoppingCart = mock(ShoppingCart.class);
        when(cartItemRepository.findById(id)).thenReturn(Optional.of(cartItem));
        when(shoppingCartRepository.findByUserEmail(username))
                .thenReturn(Optional.of(userShoppingCart));
        when(cartItem.getShoppingCart()).thenReturn(cartItemShoppingCart);
        when(cartItemShoppingCart.getId()).thenReturn(4L);
        when(userShoppingCart.getId()).thenReturn(id);
        String expected = "Access denied. You can't edit shopping cart of another user.";

        // When
        Exception actual = assertThrows(
                AuthorizationException.class,
                () -> shoppingCartService.deleteItem(id, username));

        // Then
        assertEquals(expected, actual.getMessage());
    }

    // Clear shopping cart
    @Test
    @DisplayName("Clear shopping cart with existing shopping cart")
    public void clearShoppingCart_ShoppingCartExists_AllCartItemsDeleted() {
        // Given
        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        Set<CartItem> cartItems = Set.of(mock(CartItem.class), mock(CartItem.class));
        shoppingCart.setCartItems(cartItems);
        ShoppingCart expected = mock(ShoppingCart.class);
        when(shoppingCart.getCartItems()).thenReturn(cartItems);
        when(shoppingCartRepository.save(any(ShoppingCart.class))).thenReturn(expected);

        // When
        shoppingCartService.clearShoppingCart(shoppingCart);

        // Then
        verify(cartItemRepository, times(cartItems.size())).delete(any(CartItem.class));
        assertTrue(expected.getCartItems().isEmpty());
        verify(shoppingCartRepository).save(shoppingCart);
    }

    // Create shopping cart
    @Test
    @DisplayName("Create shopping cart with existing user")
    public void createShoppingCart_UserExists_ShoppingCartCreated() {
        // Given
        ShoppingCart expected = mock(ShoppingCart.class);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(mock(User.class)));
        when(shoppingCartRepository.save(any(ShoppingCart.class))).thenReturn(expected);

        // When
        ShoppingCart actual = shoppingCartService.createShoppingCart(anyString());

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Clear shopping cart with non-existing shopping cart")
    public void createShoppingCart_UserDoesNotExist_EntityNotFoundExceptionThrown() {
        // Given
        String username = "nonExistentUser@example.com";
        when(userRepository.findByEmail(username)).thenReturn(Optional.empty());
        String expected = "Can't find user by username: " + username;

        // When
        Exception actual = assertThrows(EntityNotFoundException.class,
                () -> shoppingCartService.createShoppingCart(username));

        // Then
        assertEquals(expected, actual.getMessage());
    }
}