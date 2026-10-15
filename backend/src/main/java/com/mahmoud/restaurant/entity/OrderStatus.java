package com.mahmoud.restaurant.entity;

public enum OrderStatus {
    PENDING,
    PREPARING,
    SERVED,
    PAID,
    CANCELLED;

    public boolean isClosed() {
        return this == PAID || this == CANCELLED;
    }
}
