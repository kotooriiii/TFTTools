package com.tfttools.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record OAuthLoginRequest(
        @NotBlank String code
) {}
