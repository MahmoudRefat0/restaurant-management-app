package com.mahmoud.restaurant.dto;

import com.mahmoud.restaurant.entity.OrderItem;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long menuItemId,
        String name,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal
) {

    public static OrderItemResponse from(OrderItem item) {
        return new OrderItemResponse(
                item.getMenuItem().getId(),
                item.getMenuItem().getName(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getSubtotal());
    }
}
