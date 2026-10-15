package com.mahmoud.restaurant.dto;

import com.mahmoud.restaurant.entity.TableStatus;
import jakarta.validation.constraints.NotNull;

public record TableStatusRequest(@NotNull TableStatus status) {
}
