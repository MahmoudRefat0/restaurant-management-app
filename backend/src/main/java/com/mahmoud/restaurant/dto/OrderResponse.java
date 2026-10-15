package com.mahmoud.restaurant.dto;

import com.mahmoud.restaurant.entity.Order;
import com.mahmoud.restaurant.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        Integer tableNumber,
        OrderStatus status,
        BigDecimal totalAmount,
        String createdBy,
        LocalDateTime createdAt,
        List<OrderItemResponse> items
) {

    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getTable().getNumber(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getCreatedBy().getFullName(),
                order.getCreatedAt(),
                order.getItems().stream().map(OrderItemResponse::from).toList());
    }
}
