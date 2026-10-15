package com.mahmoud.restaurant.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TableRequest(
        @NotNull @Positive Integer number,
        @NotNull @Positive Integer capacity
) {
}
