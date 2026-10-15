package com.mahmoud.restaurant.dto;

import com.mahmoud.restaurant.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record OrderStatusRequest(@NotNull OrderStatus status) {
}
