package com.securemessenger.dto;

import com.securemessenger.model.User;

public record UserResponse(
        String id,
        String username,
        String displayName,
        String avatarId,
        String bio,
        long createdAt
) {

    public static UserResponse from(User user) {

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getProfile().getDisplayName(),
                user.getProfile().getAvatarId(),
                user.getProfile().getBio(),
                user.getCreatedAt()
        );
    }
}