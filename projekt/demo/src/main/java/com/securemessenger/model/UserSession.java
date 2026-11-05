package com.securemessenger.model;

public record UserSession(
        String id,
        String userId,
        String tokenHash,
        long createdAt,
        long expiresAt
) {
}