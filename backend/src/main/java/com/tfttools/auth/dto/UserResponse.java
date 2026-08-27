package com.tfttools.auth.dto;

import com.tfttools.auth.domain.User;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String username,
        String email,
        Instant createdAt
) {
    public static UserResponse from(User user)
    {
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getCreatedAt());
    }
}
