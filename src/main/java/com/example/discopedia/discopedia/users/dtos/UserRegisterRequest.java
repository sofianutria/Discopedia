package com.example.discopedia.discopedia.users.dtos;

import jakarta.validation.constraints.*;

public record UserRegisterRequest(
        @NotBlank(message="Username is required")
        @Size(min=3, max=50, message = "Username must be between 3 and 50 characters")
        String username,

        @Email(message="Email should be valid", regexp="regexp = \"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\\\.[a-zA-Z]{2,}$")
        @NotBlank(message="Email is required")
        @Size (min=3, max=100, message = "Email must be between 3 and 100 characters")
        String email,

        @NotEmpty(message = "Password is required")
        @Pattern(message = "Password must contain a minimum of 8 characters and a max of 50 characters, including a number, one uppercase letter, one lowercase letter and one special character", regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+=\\-{}\\[\\]:;\"'<>,.?/~])(?=\\S+$).{8,50}$")
        String password
) {
}
