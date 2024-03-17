package com.project.bookstore.service.order;

import com.project.bookstore.dto.order.CreateOrderRequestDto;
import com.project.bookstore.dto.order.OrderDto;
import com.project.bookstore.dto.order.OrderWithoutItemsDto;
import com.project.bookstore.dto.order.UpdateOrderStatusDto;
import com.project.bookstore.dto.order.item.OrderItemDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderDto save(String username, CreateOrderRequestDto requestDto);

    List<OrderDto> findAll(String username, Pageable pageable);

    List<OrderItemDto> getItems(Long orderId, String username);

    OrderWithoutItemsDto updateStatus(Long orderId, UpdateOrderStatusDto updateDto);

    OrderItemDto findOrderItemById(Long orderId, Long itemId, String username);
}
