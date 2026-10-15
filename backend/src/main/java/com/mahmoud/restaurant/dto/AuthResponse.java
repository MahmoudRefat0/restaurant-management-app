package com.mahmoud.restaurant.dto;

import com.mahmoud.restaurant.entity.Role;

public record AuthResponse(String token, String username, String fullName, Role role) {
}
