package com.securemessenger.dto;

public record UpdateProfileRequest(
        String username,
        String displayName,
        String bio
) {
}