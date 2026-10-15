package com.mahmoud.restaurant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record MenuItemRequest(
        @NotBlank @Size(max = 100) String name,
        String description,
        @NotNull @Positive BigDecimal price,
        @NotNull Long categoryId,
        boolean available
) {
}
