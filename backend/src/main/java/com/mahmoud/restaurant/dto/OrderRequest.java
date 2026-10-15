package com.mahmoud.restaurant.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderRequest(
        @NotNull Long tableId,
        @NotEmpty List<@Valid OrderItemRequest> items
) {
}
