package com.mahmoud.restaurant.dto;

import com.mahmoud.restaurant.entity.RestaurantTable;
import com.mahmoud.restaurant.entity.TableStatus;

public record TableResponse(Long id, Integer number, Integer capacity, TableStatus status) {

    public static TableResponse from(RestaurantTable table) {
        return new TableResponse(table.getId(), table.getNumber(), table.getCapacity(), table.getStatus());
    }
}
