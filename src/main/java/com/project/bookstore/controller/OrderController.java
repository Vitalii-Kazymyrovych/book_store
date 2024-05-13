package com.project.bookstore.controller;

import com.project.bookstore.config.SwaggerConstants;
import com.project.bookstore.dto.order.CreateOrderRequestDto;
import com.project.bookstore.dto.order.OrderDto;
import com.project.bookstore.dto.order.OrderWithoutItemsDto;
import com.project.bookstore.dto.order.UpdateOrderStatusDto;
import com.project.bookstore.dto.order.item.OrderItemDto;
import com.project.bookstore.service.order.OrderService;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

import static com.project.bookstore.config.SwaggerConstants.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Orders management endpoints")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    @PreAuthorize("hasAuthority('user')")
    @Operation(summary = FIND_ALL_ORDERS_SUM, description = FIND_ALL_ORDERS_DESC)
    public List<OrderDto> findAll(
            Authentication authentication,
            Pageable pageable) {
        return orderService.findAll(authentication.getName(), pageable);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('user')")
    @Operation(summary = SAVE_ORDER_SUM, description = SAVE_ORDER_DESC)
    public OrderDto save(
            Authentication authentication,
            @RequestBody CreateOrderRequestDto requestDto) {
        return orderService.save(authentication.getName(), requestDto);
    }

    @GetMapping("/{orderId}/items")
    @PreAuthorize("hasAuthority('user')")
    @Operation(summary = GET_ORDER_ITEMS_SUM, description = GET_ORDER_ITEMS_DESC)
    public List<OrderItemDto> getItems(
            @PathVariable Long orderId,
            Authentication authentication) {
        return orderService.getItems(orderId, authentication.getName());
    }

    @GetMapping("/{orderId}/items/{itemId}")
    @PreAuthorize("hasAuthority('user')")
    @Operation(summary = FIND_ORDER_ITEM_BY_ID_SUM, description = FIND_ORDER_ITEM_BY_ID_DESC)
    public OrderItemDto findOrderItemById(
            @PathVariable Long orderId,
            @PathVariable Long itemId,
            Authentication authentication) {
        return orderService.findOrderItemById(
                orderId,
                itemId,
                authentication.getName());
    }

    @PutMapping("/{orderId}")
    @PreAuthorize("hasAuthority('admin')")
    @Operation(summary = UPDATE_ORDER_STATUS_SUM, description = UPDATE_ORDER_STATUS_DESC)
    public OrderWithoutItemsDto updateStatus(
            @PathVariable Long orderId,
            @RequestBody UpdateOrderStatusDto updateDto) {
        return orderService.updateStatus(orderId, updateDto);
    }
}
