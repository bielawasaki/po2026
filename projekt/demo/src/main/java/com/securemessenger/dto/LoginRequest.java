package com.securemessenger.dto;

public record LoginRequest(
        String username,
        String password
) {
}