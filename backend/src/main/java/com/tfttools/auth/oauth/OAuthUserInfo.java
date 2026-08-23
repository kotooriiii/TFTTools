package com.tfttools.auth.oauth;

/**
 * Identity claims read from a verified OAuth provider token.
 */
public record OAuthUserInfo(
        String subjectId,
        String email,
        boolean emailVerified,
        String displayName
) {}
