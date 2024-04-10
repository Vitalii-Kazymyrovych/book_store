package com.project.bookstore.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import com.project.bookstore.dto.order.CreateOrderRequestDto;
import com.project.bookstore.dto.order.OrderDto;
import com.project.bookstore.dto.order.OrderWithoutItemsDto;
import com.project.bookstore.dto.order.UpdateOrderStatusDto;
import com.project.bookstore.dto.order.item.OrderItemDto;
import com.project.bookstore.exception.AuthorizationException;
import com.project.bookstore.exception.EntityNotFoundException;
import com.project.bookstore.mapper.OrderItemMapper;
import com.project.bookstore.mapper.OrderMapper;
import com.project.bookstore.mapper.ShoppingCartMapper;
import com.project.bookstore.model.Order;
import com.project.bookstore.model.OrderItem;
import com.project.bookstore.model.ShoppingCart;
import com.project.bookstore.model.User;
import com.project.bookstore.repository.order.OrderRepository;
import com.project.bookstore.repository.order.item.OrderItemRepository;
import com.project.bookstore.repository.shopping.cart.ShoppingCartRepository;
import com.project.bookstore.service.order.OrderServiceImpl;
import com.project.bookstore.service.shopping.cart.ShoppingCartServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private ShoppingCartServiceImpl shoppingCartService;
    @Mock
    private ShoppingCartMapper shoppingCartMapper;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private OrderItemMapper orderItemMapper;
    @Mock
    private OrderMapper orderMapper;
    @InjectMocks
    private OrderServiceImpl orderServiceImpl;

    //Save
    @Test
    @DisplayName("Save with valid username and request dto")
    void save_ValidUsernameAndRequestDto_ReturnsOrderDto() {
        // Given
        String username = "user@example.com";
        CreateOrderRequestDto requestDto = mock(CreateOrderRequestDto.class);
        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        Order order = mock(Order.class);
        OrderDto expected = mock(OrderDto.class);
        when(shoppingCartRepository.findWithCartItemsByUserEmail(username))
                .thenReturn(Optional.of(shoppingCart));
        when(shoppingCartMapper.toOrder(shoppingCart))
                .thenReturn(order);
        when(orderRepository.save(order))
                .thenReturn(order);
        when(orderMapper.toDto(order))
                .thenReturn(expected);

        // When
        OrderDto actual = orderServiceImpl.save(username, requestDto);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Save with invalid username and valid request dto")
    void save_InvalidUsername_ThrowsNullPointerException() {
        // Given
        CreateOrderRequestDto requestDto
                = mock(CreateOrderRequestDto.class);
        String expected = "Cannot invoke \"com.project."
                + "bookstore.model.Order.setShippingAddress"
                + "(String)\" because \"newOrder\" is null";
        when(shoppingCartRepository.findWithCartItemsByUserEmail(anyString()))
                .thenReturn(Optional.empty());

        // When
        Exception actual = assertThrows(NullPointerException.class, () -> {
            orderServiceImpl.save(anyString(), requestDto);});

        // Then
        assertEquals(expected, actual.getMessage());
    }

    @Test
    @DisplayName("Save with null username and valid request dto")
    void save_NullUsername_ThrowsNullPointerException() {
        // Given
        String username = null;
        CreateOrderRequestDto requestDto = mock(CreateOrderRequestDto.class);
        String expected = "Cannot invoke \"com.project."
                + "bookstore.model.Order.setShippingAddress"
                + "(String)\" because \"newOrder\" is null";

        // When
        Exception actual = assertThrows(NullPointerException.class, () ->
            orderServiceImpl.save(username, requestDto));

        // Then
        assertEquals(expected, actual.getMessage());
    }

    @Test
    @DisplayName("Save with valid username and null request dto")
    void save_NullRequestDto_ThrowsNullPointerException() {
        // Given
        CreateOrderRequestDto requestDto = null;
        String expected = "Cannot invoke \"com.project."
                + "bookstore.dto.order.CreateOrderRequestDto."
                + "shippingAddress()\" because \"requestDto\" "
                + "is null";

        // When
        Exception actual = assertThrows(NullPointerException.class, () ->
            orderServiceImpl.save(anyString(), requestDto));

        // Then
        assertEquals(expected, actual.getMessage());
    }

    //Find all
    @Test
    @DisplayName("Find all with valid username and valid pageable")
    void findAll_ValidUsernameAndPageable_ReturnsListOfOrderDto() {
        // Given
        String username = "valid@example.com";
        Order order = mock(Order.class);
        List<Order> list = List.of(order, order);
        Pageable pageable = mock(Pageable.class);
        Page<Order> orderPage = new PageImpl<>(list, pageable, list.size());
        OrderDto orderDto = mock(OrderDto.class);
        List<OrderDto> expected = List.of(orderDto, orderDto);
        when(orderRepository.findAllWithItemsByUserEmail(username, pageable)).thenReturn(orderPage);
        when(orderMapper.toDto(any(Order.class))).thenReturn(orderDto);

        // When
        List<OrderDto> actual = orderServiceImpl.findAll(username, pageable);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Find all with invalid username and valid pageable")
    void findAll_InvalidUsername_ThrowsNullPointerException() {
        // Given
        String username = "invalid@example.com";
        Pageable pageable = mock(Pageable.class);
        List<Order> list = Collections.emptyList();
        Page<Order> page = new PageImpl<>(list, pageable, list.size());
        when(orderRepository.findAllWithItemsByUserEmail(username, pageable))
                .thenReturn(page);
        List<OrderDto> expected = Collections.emptyList();

        // When
        List<OrderDto> actual = orderServiceImpl.findAll(username, pageable);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Find all with null username and valid pageable")
    void findAll_NullUsername_ThrowsNullPointerException() {
        // Given
        String username = null;
        Pageable pageable = mock(Pageable.class);
        String expected = "Cannot invoke \"org.springframework."
                + "data.domain.Page.stream()\" because the return "
                + "value of \"com.project.bookstore.repository."
                + "order.OrderRepository.findAllWithItemsByUserEmail"
                + "(String, org.springframework.data.domain.Pageable)"
                + "\" is null";

        // When
        Exception actual = assertThrows(NullPointerException.class,
                () -> orderServiceImpl.findAll(username, pageable));

        // Then
        assertEquals(expected, actual.getMessage());
    }

    @Test
    @DisplayName("Find all with valid username and null pageable")
    void findAll_NullPageable_ThrowsNullPointerException() {
        // Given
        String username = "validUsername";
        Pageable pageable = null;
        String expected = "Cannot invoke \"org.springframework."
                + "data.domain.Page.stream()\" because the "
                + "return value of \"com.project.bookstore."
                + "repository.order.OrderRepository."
                + "findAllWithItemsByUserEmail(String, "
                + "org.springframework.data.domain.Pageable)"
                + "\" is null";

        // When
        Exception actual = assertThrows(NullPointerException.class,
                () -> orderServiceImpl.findAll(username, pageable));

        // Then
        assertEquals(expected, actual.getMessage());
    }

    @Test
    @DisplayName("Find all with valid username and empty pageable")
    void findAll_EmptyPageable_ReturnsEmptyListOfOrderDto() {
        // Given
        String username = "test@example.com";
        Pageable pageable = Pageable.unpaged();
        when(orderRepository.findAllWithItemsByUserEmail(
                username, pageable))
                .thenReturn(Page.empty());
        List<OrderDto> expected = Collections.emptyList();

        // When
        List<OrderDto> actual = orderServiceImpl.findAll(username, pageable);

        // Then
        assertEquals(expected, actual);
    }

    //Get items
    @Test
    @DisplayName("Get items with valid order id and valid username")
    void getItems_ValidOrderIdAndUsername_ReturnsListOfOrderItemDto() {
        // Given
        Long id = 1L;
        String username = "validUsername";
        Order order = mock(Order.class);
        User user = mock(User.class);
        OrderItem orderItem = mock(OrderItem.class);
        OrderItem orderItem1 = mock(OrderItem.class);
        Set<OrderItem> orderItems = Set.of(orderItem, orderItem1);
        OrderItemDto orderItemDto = mock(OrderItemDto.class);
        List<OrderItemDto> expected = List.of(orderItemDto, orderItemDto);
        when(orderRepository.findWithItemsAndUserById(anyLong()))
                .thenReturn(Optional.of(order));
        when(order.getUser()).thenReturn(user);
        when(user.getEmail()).thenReturn(username);
        when(order.getOrderItems()).thenReturn(orderItems);
        when(orderItemMapper.toDto(any(OrderItem.class)))
                .thenReturn(orderItemDto);

        // When
        List<OrderItemDto> actual = orderServiceImpl.getItems(id, username);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Get items with invalid order id and valid username")
    void getItems_InvalidOrderId_ThrowsEntityNotFoundException() {
        // Given
        String username = "user@example.com";
        when(orderRepository.findWithItemsAndUserById(anyLong()))
                .thenReturn(Optional.empty());
        String expected = "Can't find order by id: 0";

        // When
        Exception actual = assertThrows(EntityNotFoundException.class,
                () -> orderServiceImpl.getItems(anyLong(), username));

        // Then
        assertEquals(expected, actual.getMessage());
    }

    @Test
    @DisplayName("Get items with null order id and valid username")
    void getItems_NullOrderId_ThrowsEntityNotFoundException() {
        // Given
        Long orderId = null;
        String username = "test@example.com";
        String expected = "Can't find order by id: null";

        // When
        Exception actual = assertThrows(EntityNotFoundException.class,
                () -> orderServiceImpl.getItems(orderId, username));

        // Then
        assertEquals(expected, actual.getMessage());
    }

    @Test
    @DisplayName("Get items with valid order id and invalid username")
    void getItems_InvalidUsername_ThrowsAuthorizationException() {
        // Given
        String username = "wrong@example.com";
        String correctUsername = "correctUsername";
        Order order = mock(Order.class);
        User user = mock(User.class);
        when(orderRepository.findWithItemsAndUserById(anyLong()))
                .thenReturn(Optional.of(order));
        when(order.getUser()).thenReturn(user);
        when(user.getEmail()).thenReturn(correctUsername);
        String expected = "Access denied. You cannot view another user's orders.";

        // When
        Exception actual = assertThrows(AuthorizationException.class,
                () -> orderServiceImpl.getItems(anyLong(), username));

        // Then
        assertEquals(expected, actual.getMessage());
    }

    @Test
    @DisplayName("Get items with valid order id and null username")
    void getItems_NullUsername_ThrowsEntityNotFoundException() {
        // Given
        String username = null;
        String expected = "Can't find order by id: 0";

        // When
        Exception actual = assertThrows(EntityNotFoundException.class,
                () -> orderServiceImpl.getItems(anyLong(), username));

        // Then
        assertEquals(expected, actual.getMessage());
    }

    @Test
    @DisplayName("Update status with valid order id and valid update dto")
    void updateStatus_ValidOrderIdAndUpdateDto_ReturnsOrderWithoutItemsDto() {
        // Given
        UpdateOrderStatusDto updateDto = mock(UpdateOrderStatusDto.class);
        Order order = mock(Order.class);
        OrderWithoutItemsDto expected = mock(OrderWithoutItemsDto.class);
        when(updateDto.status()).thenReturn("paid");
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        when(orderMapper.toOrderWithoutItemsDto(order)).thenReturn(expected);
        when(orderRepository.save(order)).thenReturn(order);

        // When
        OrderWithoutItemsDto actual = orderServiceImpl.updateStatus(anyLong(), updateDto);

        // Then
        assertEquals(expected, actual);
    }

   @Test
   @DisplayName("Update status with invalid order id and valid update dto")
   void updateStatus_InvalidOrderId_EntityNotFoundException() {
        // Given
        UpdateOrderStatusDto updateDto = mock(UpdateOrderStatusDto.class);
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());
        String expected = "Can't find order by id: 0";

        // When
        Exception actual = assertThrows(EntityNotFoundException.class,
                () -> orderServiceImpl.updateStatus(anyLong(), updateDto));

        // Then
        assertEquals(expected, actual.getMessage());


    }

    @Test
    @DisplayName("Update status with null order id and valid update dto")
    void updateStatus_NullOrderId_ThrowsEntityNotFoundException() {
        // Given
        Long orderId = null;
        UpdateOrderStatusDto updateDto = mock(UpdateOrderStatusDto.class);
        String expected = "Can't find order by id: null";

        // When
        Exception actual = assertThrows(EntityNotFoundException.class,
                () -> orderServiceImpl.updateStatus(orderId, updateDto));

        // Then
        assertEquals(expected, actual.getMessage());
    }

    @Test
    @DisplayName("Update status with valid order id and null update dto")
    void updateStatus_NullUpdateDto_ThrowsNullPointerException() {
        // Given
        UpdateOrderStatusDto updateDto = null;
        String expected = "Cannot invoke \"com.project."
                + "bookstore.dto.order.UpdateOrderStatusDto."
                + "status()\" because \"updateDto\" is null";
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.of(mock(Order.class)));

        // When
        Exception actual = assertThrows(NullPointerException.class,
                () -> orderServiceImpl.updateStatus(anyLong(), updateDto));

        // Then
        assertEquals(expected, actual.getMessage());
    }

    @Test
    @DisplayName("Update status with valid order id and empty update dto")
    void updateStatus_EmptyUpdateDto_ThrowsIllegalArgumentException() {
        // Given
        UpdateOrderStatusDto updateDto = mock(UpdateOrderStatusDto.class);
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.of(mock(Order.class)));
        when(updateDto.status()).thenReturn("");
        String expected = "Invalid status name: ";

        // When
        Exception actual = assertThrows(IllegalArgumentException.class,
                () -> orderServiceImpl.updateStatus(anyLong(), updateDto));

        // Then
        assertEquals(expected, actual.getMessage());
    }

    @Test
    @DisplayName("Find order item by id with valid " +
            "order id, valid item id and valid username")
    void findOrderItemById_ValidOrderIdAndItemIdAndUsername_ReturnsOrderItemDto() {
        // Given
        Long id = 1L;
        String username = "user@example.com";
        Order order = mock(Order.class);
        User user = mock(User.class);
        OrderItem orderItem = mock(OrderItem.class);
        OrderItemDto expected = mock(OrderItemDto.class);
        when(order.getUser()).thenReturn(user);
        when(user.getEmail()).thenReturn(username);
        when(order.getOrderItems()).thenReturn(Set.of(orderItem));
        when(orderRepository.findWithItemsAndUserById(anyLong())).thenReturn(Optional.of(order));
        when(orderItemMapper.toDto(orderItem)).thenReturn(expected);
        when(expected.id()).thenReturn(id);

        // When
        OrderItemDto actual = orderServiceImpl.findOrderItemById(id, id, username);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Find order item by id with invalid " +
            "order id, valid item id and valid username")
    void findOrderItemById_InvalidOrderId_EntityNotFoundException() {
        // Given
        Long id = 1L;
        String username = "user@example.com";
        when(orderRepository.findWithItemsAndUserById(id)).thenReturn(Optional.empty());
        String expected = "Can't find order by id: 1";

        // When
        Exception actual = assertThrows(EntityNotFoundException.class,
                () -> orderServiceImpl.findOrderItemById(id, id, username));

        // Then
        assertEquals(expected, actual.getMessage());
    }

    @Test
    @DisplayName("Find order item by id with null " +
            "order id, valid item id and valid username")
    void findOrderItemById_NullOrderId_ThrowsEntityNotFoundException() {
        // Given
        Long orderId = null;
        Long itemId = 1L;
        String username = "user@example.com";
        String expected = "Can't find order by id: null";

        // When
        Exception actual = assertThrows(EntityNotFoundException.class,
                () -> orderServiceImpl.findOrderItemById(orderId, itemId, username));

        // Then
        assertEquals(expected, actual.getMessage());
    }

    @Test
    @DisplayName("Find order item by id with invalid " +
            "order id, invalid item id and valid username")
    void findOrderItemById_InvalidItemId_ThrowsIllegalArgumentException() {
        // Given
        Long id = 1L;
        String username = "user@example.com";
        Order order = mock(Order.class);
        User user = mock(User.class);
        when(orderRepository.findWithItemsAndUserById(id)).thenReturn(Optional.of(order));
        when(order.getUser()).thenReturn(user);
        when(user.getEmail()).thenReturn(username);
        when(order.getOrderItems()).thenReturn(Set.of());
        String expected = "Invalid orderItem id: 1";

        // When
        Exception actual = assertThrows(IllegalArgumentException.class,
                () -> orderServiceImpl.findOrderItemById(id, id, username));

        // Then
        assertEquals(expected, actual.getMessage());
    }

    @Test
    @DisplayName("Find order item by id with invalid " +
            "order id, null item id and valid username")
    void findOrderItemById_NullItemId_ThrowsEntityNotFoundException() {
        // Given
        Long orderId = 1L;
        Long itemId = null;
        String username = "user@example.com";
        String expected = "Can't find order by id: 1";

        // When
        Exception actual = assertThrows(EntityNotFoundException.class,
                () -> orderServiceImpl.findOrderItemById(orderId, itemId, username));

        // Then
        assertEquals(expected, actual.getMessage());
    }

    @Test
    @DisplayName("Find order item by id with invalid " +
            "order id, valid item id and invalid username")
    void findOrderItemById_InvalidUsername_ThrowsAuthorizationException() {
        // Given
        Long id = 1L;
        String invalidUsername = "invalid@example.com";
        Order order = mock(Order.class);
        User user = mock(User.class);
        when(orderRepository.findWithItemsAndUserById(id)).thenReturn(Optional.of(order));
        when(order.getUser()).thenReturn(user);
        when(user.getEmail()).thenReturn("valid@example.com");
        String expected = "Access denied. You cannot view another user's orders.";

        // When
        Exception actual = assertThrows(AuthorizationException.class,
                () -> orderServiceImpl.findOrderItemById(id, id, invalidUsername));

        // Then
        assertEquals(expected, actual.getMessage());
    }

    @Test
    @DisplayName("Find order item by id with invalid " +
            "order id, valid item id and null username")
    void findOrderItemById_NullUsername_ThrowsEntityNotFoundException() {
        // Given
        Long id = 1L;
        String nullUsername = null;
        String expected = "Can't find order by id: 1";
        // When
        Exception actual = assertThrows(EntityNotFoundException.class,
                () -> orderServiceImpl.findOrderItemById(id, id, nullUsername));

        // Then
        assertEquals(expected, actual.getMessage());
    }
}