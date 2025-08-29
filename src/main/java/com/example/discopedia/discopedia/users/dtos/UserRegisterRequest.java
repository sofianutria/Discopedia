package com.example.discopedia.discopedia.users.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegisterRequest(
        @NotBlank(message="Username is required")
        @Size(min=3, max=50, message = "Username must be between 3 and 50 characters")
        String username,

        @Email(message="Email should be valid", regexp="regexp = \"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\\\.[a-zA-Z]{2,}$")
        @NotBlank(message="Email is required")
        @Size (min=3, max=100, message = "Email must be between 3 and 100 characters")
        String email


) {
}
