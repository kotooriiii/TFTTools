package com.tfttools.auth.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * "users" rather than "user" - "user" is a reserved identifier in Postgres.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
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

    @Column(unique = true)
    private String oauthSubjectId;

    @Column(nullable = false)
    private Instant createdAt;
}
