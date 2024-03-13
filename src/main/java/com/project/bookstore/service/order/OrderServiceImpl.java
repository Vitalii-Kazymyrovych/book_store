package com.project.bookstore.service.order;

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
import com.project.bookstore.repository.order.OrderRepository;
import com.project.bookstore.repository.order.item.OrderItemRepository;
import com.project.bookstore.repository.shopping.cart.ShoppingCartRepository;
import com.project.bookstore.service.shopping.cart.ShoppingCartService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ShoppingCartService shoppingCartService;
    private final ShoppingCartMapper shoppingCartMapper;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    public OrderDto save(String username, CreateOrderRequestDto requestDto) {
        ShoppingCart shoppingCart =
                shoppingCartRepository.findWithCartItemsByUserEmail(username)
                        .orElseGet(
                                () -> shoppingCartService
                                        .createShoppingCart(username));
        Order savedOrder = orderRepository
                .save(initializeNewOrder(shoppingCart, requestDto));
        orderItemRepository.saveAll(savedOrder.getOrderItems()
                .stream()
                .peek(orderItem -> orderItem.setOrder(savedOrder))
                .collect(Collectors.toSet()));
        shoppingCartService.clearShoppingCart(shoppingCart);
        return orderMapper.toDto(savedOrder);
    }

    @Override
    public List<OrderDto> findAll(String username, Pageable pageable) {
        return orderRepository.findAllWithItemsByUserEmail(username, pageable)
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public List<OrderItemDto> getItems(Long orderId, String username) {
        Order order = orderRepository.findWithItemsAndUserById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find order by id: " + orderId));
        if (!Objects.equals(order.getUser().getEmail(), username)) {
            throw new AuthorizationException(
                    "Access denied. You cannot view another user's orders.");
        }
        return order.getOrderItems()
                .stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderWithoutItemsDto updateStatus(
            Long orderId,
            UpdateOrderStatusDto updateDto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find order by id: " + orderId));
        Order.Status newStatus = Arrays.stream(Order.Status.values())
                .filter(st -> st.toString().equalsIgnoreCase(updateDto.status()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Invalid status name: "
                                + updateDto.status()));
        order.setStatus(newStatus);
        return orderMapper.toOrderWithoutItemsDto(orderRepository.save(order));
    }

    @Override
    public OrderItemDto findOrderItemById(Long orderId, Long itemId, String username) {
        Order order = orderRepository.findWithItemsAndUserById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find order by id: " + orderId));
        if (!Objects.equals(order.getUser().getEmail(), username)) {
            throw new AuthorizationException(
                    "Access denied. You cannot view another user's orders.");
        }
        return order.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .filter(item -> Objects.equals(item.id(), itemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Invalid orderItem id: "
                                + itemId));
    }

    private Order initializeNewOrder(ShoppingCart shoppingCart, CreateOrderRequestDto requestDto) {
        Order newOrder = shoppingCartMapper.toOrder(shoppingCart);
        newOrder.setShippingAddress(requestDto.shippingAddress());
        newOrder.setOrderDate(LocalDateTime.now());
        newOrder.setStatus(Order.Status.NEW);
        newOrder.setTotal(countTotal(newOrder));
        return newOrder;
    }

    private BigDecimal countTotal(Order newOrder) {
        return newOrder.getOrderItems()
                .stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
