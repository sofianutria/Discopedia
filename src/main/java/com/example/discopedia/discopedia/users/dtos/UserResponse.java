package com.example.discopedia.discopedia.users.dtos;

public record UserResponse(
        Long id,
        String username,
        String email,
        String role
) {
}
