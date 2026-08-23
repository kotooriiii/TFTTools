package com.tfttools.auth.dto;

public record AuthResponse(
        String token,
        UserResponse user
) {}
