package com.securemessenger.dto;

public record AuthResponse(
        String token,
        UserResponse user
) {
}