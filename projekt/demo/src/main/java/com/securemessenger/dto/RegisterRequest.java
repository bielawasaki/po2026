package com.securemessenger.dto;

public record RegisterRequest(
        String username,
        String password
) {
}