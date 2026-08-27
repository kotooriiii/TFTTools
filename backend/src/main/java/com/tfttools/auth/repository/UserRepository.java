package com.tfttools.auth.repository;

import com.tfttools.auth.domain.User;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Hand-written SQL over {@link JdbcClient}, backed by Postgres and schema-managed by Flyway
 * ({@code db/migration}) - unrelated to the in-memory, Community-Dragon-backed repositories in
 * {@code com.tfttools.repository} (e.g. TraitRepository, UnitRepository).
 */
@Repository
public class UserRepository
{
    private final JdbcClient jdbcClient;

    public UserRepository(JdbcClient jdbcClient)
    {
        this.jdbcClient = jdbcClient;
    }

    public Optional<User> findByEmail(String email)
    {
        return jdbcClient.sql("SELECT * FROM users WHERE email = :email")
                .param("email", email)
                .query(this::mapRow)
                .optional();
    }

    public boolean existsByEmail(String email)
    {
        return jdbcClient.sql("SELECT EXISTS(SELECT 1 FROM users WHERE email = :email)")
                .param("email", email)
                .query(Boolean.class)
                .single();
    }

    public Optional<User> findByOauthProviderAndOauthSubjectId(String oauthProvider, String oauthSubjectId)
    {
        return jdbcClient.sql("SELECT * FROM users WHERE oauth_provider = :oauthProvider AND oauth_subject_id = :oauthSubjectId")
                .param("oauthProvider", oauthProvider)
                .param("oauthSubjectId", oauthSubjectId)
                .query(this::mapRow)
                .optional();
    }

    public Optional<User> findById(UUID id)
    {
        return jdbcClient.sql("SELECT * FROM users WHERE id = :id")
                .param("id", id)
                .query(this::mapRow)
                .optional();
    }

    public User save(User user)
    {
        return user.getId() == null ? insert(user) : update(user);
    }

    private User insert(User user)
    {
        user.setCreatedAt(Instant.now());
        return jdbcClient.sql("""
                INSERT INTO users (username, email, password_hash, oauth_provider, oauth_subject_id, created_at)
                VALUES (:username, :email, :passwordHash, :oauthProvider, :oauthSubjectId, :createdAt)
                RETURNING *
                """)
                .param("username", user.getUsername())
                .param("email", user.getEmail())
                .param("passwordHash", user.getPasswordHash())
                .param("oauthProvider", user.getOauthProvider())
                .param("oauthSubjectId", user.getOauthSubjectId())
                .param("createdAt", Timestamp.from(user.getCreatedAt()))
                .query(this::mapRow)
                .single();
    }

    private User update(User user)
    {
        return jdbcClient.sql("""
                UPDATE users
                SET username = :username, email = :email, password_hash = :passwordHash,
                    oauth_provider = :oauthProvider, oauth_subject_id = :oauthSubjectId
                WHERE id = :id
                RETURNING *
                """)
                .param("id", user.getId())
                .param("username", user.getUsername())
                .param("email", user.getEmail())
                .param("passwordHash", user.getPasswordHash())
                .param("oauthProvider", user.getOauthProvider())
                .param("oauthSubjectId", user.getOauthSubjectId())
                .query(this::mapRow)
                .single();
    }

    private User mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        User user = new User();
        user.setId(rs.getObject("id", UUID.class));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setOauthProvider(rs.getString("oauth_provider"));
        user.setOauthSubjectId(rs.getString("oauth_subject_id"));
        user.setCreatedAt(rs.getObject("created_at", java.time.OffsetDateTime.class).toInstant());
        return user;
    }
}
