package com.project.bookstore.controller;

import com.project.bookstore.dto.order.CreateOrderRequestDto;
import com.project.bookstore.dto.order.OrderDto;
import com.project.bookstore.dto.order.OrderWithoutItemsDto;
import com.project.bookstore.dto.order.UpdateOrderStatusDto;
import com.project.bookstore.dto.order.item.OrderItemDto;
import com.project.bookstore.service.order.OrderService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PreAuthorize("hasAuthority('user')")
    @GetMapping
    public List<OrderDto> findAll(
            Authentication authentication,
            Pageable pageable) {
        return orderService.findAll(authentication.getName(), pageable);
    }

    @PreAuthorize("hasAuthority('user')")
    @PostMapping
    public OrderDto save(
            Authentication authentication,
            @RequestBody CreateOrderRequestDto requestDto) {
        return orderService.save(authentication.getName(), requestDto);
    }

    @PreAuthorize("hasAuthority('user')")
    @GetMapping("/{orderId}/items")
    public List<OrderItemDto> getItems(
            @PathVariable Long orderId,
            Authentication authentication) {
        return orderService.getItems(orderId, authentication.getName());
    }

    @PreAuthorize("hasAuthority('user')")
    @GetMapping("/{orderId}/items/{itemId}")
    public OrderItemDto findOrderItemById(
            @PathVariable Long orderId,
            @PathVariable Long itemId,
            Authentication authentication) {
        return orderService.findOrderItemById(
                orderId,
                itemId,
                authentication.getName());
    }

    @PreAuthorize("hasAuthority('admin')")
    @PutMapping("/{orderId}")
    public OrderWithoutItemsDto updateStatus(
            @PathVariable Long orderId,
            @RequestBody UpdateOrderStatusDto updateDto) {
        return orderService.updateStatus(orderId, updateDto);
    }
}
