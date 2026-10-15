package com.mahmoud.restaurant.dto;

import com.mahmoud.restaurant.entity.MenuItem;

import java.math.BigDecimal;

public record MenuItemResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        boolean available,
        Long categoryId,
        String categoryName
) {

    public static MenuItemResponse from(MenuItem item) {
        return new MenuItemResponse(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                item.isAvailable(),
                item.getCategory().getId(),
                item.getCategory().getName());
    }
}
