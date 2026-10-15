package com.mahmoud.restaurant.dto;

import com.mahmoud.restaurant.entity.Role;
import com.mahmoud.restaurant.entity.User;

public record UserResponse(Long id, String username, String fullName, Role role) {

    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getFullName(), user.getRole());
    }
}
