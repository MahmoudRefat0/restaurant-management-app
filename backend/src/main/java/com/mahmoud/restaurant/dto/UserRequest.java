package com.mahmoud.restaurant.dto;

import com.mahmoud.restaurant.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @NotBlank @Size(max = 50) String username,
        @NotBlank @Size(min = 6) String password,
        @NotBlank @Size(max = 100) String fullName,
        @NotNull Role role
) {
}
