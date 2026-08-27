package com.tfttools.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/**
 * "users" rather than "user" - "user" is a reserved identifier in Postgres.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User
{
    private UUID id;

    private String username;

    private String email;

    /**
     * Null for accounts that have only ever signed in via an OAuth provider.
     */
    private String passwordHash;

    /**
     * Null unless an OAuth identity has been linked (via OAuth signup, or auto-linking
     * an OAuth sign-in to an existing password account with a matching verified email).
     */
    private String oauthProvider;

    private String oauthSubjectId;

    private Instant createdAt;
}
