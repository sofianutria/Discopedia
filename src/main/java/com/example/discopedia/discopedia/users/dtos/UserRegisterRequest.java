package com.example.discopedia.discopedia.users.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegisterRequest(
        @NotBlank(message="Username is required")
        @Size(min=3, max=50, message = "Username must be between 3 and 50 characters")
        String username



) {
}
