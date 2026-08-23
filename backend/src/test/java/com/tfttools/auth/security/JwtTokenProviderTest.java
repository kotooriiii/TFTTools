package com.tfttools.auth.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenProviderTest
{
    private static final String SECRET = "unit-test-secret-key-at-least-32-bytes-long!!";
    private static final long EXPIRATION_MS = 60_000L;

    private final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(SECRET, EXPIRATION_MS);

    @Test
    void generateAndValidate_roundTripsToSameUserId()
    {
        Long userId = 42L;

        String token = jwtTokenProvider.generateToken(userId);

        assertThat(jwtTokenProvider.validateAndGetUserId(token)).isEqualTo(userId);
    }

    @Test
    void validateAndGetUserId_returnsNullForGarbageToken()
    {
        assertThat(jwtTokenProvider.validateAndGetUserId("not-a-jwt")).isNull();
    }

    @Test
    void validateAndGetUserId_returnsNullForTokenSignedWithDifferentKey()
    {
        SecretKey otherKey = Keys.hmacShaKeyFor("a-completely-different-secret-key-32bytes!".getBytes(StandardCharsets.UTF_8));
        String tokenSignedElsewhere = Jwts.builder()
                .subject("42")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(otherKey)
                .compact();

        assertThat(jwtTokenProvider.validateAndGetUserId(tokenSignedElsewhere)).isNull();
    }

    @Test
    void validateAndGetUserId_returnsNullForExpiredToken()
    {
        SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
        Date past = new Date(System.currentTimeMillis() - 10_000L);
        String expiredToken = Jwts.builder()
                .subject("42")
                .issuedAt(new Date(past.getTime() - 1_000L))
                .expiration(past)
                .signWith(key)
                .compact();

        assertThat(jwtTokenProvider.validateAndGetUserId(expiredToken)).isNull();
    }
}
