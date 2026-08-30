package com.tfttools.comps.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfttools.comps.domain.Comp;
import com.tfttools.comps.domain.Placement;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Hand-written SQL over {@link JdbcClient}, mirroring {@code auth.repository.UserRepository}'s
 * style - backed by Postgres, schema-managed by Flyway ({@code db/migration}).
 * <p>
 * {@code placements} is stored as a JSONB column. Writes bind the serialized JSON as a plain
 * String with {@link Types#OTHER} (rather than a driver-specific {@code PGobject}), which lets
 * Postgres infer the target column type itself - the {@code postgresql} driver dependency is
 * runtime-scoped, so no driver-specific class can be referenced at compile time here. Reads use
 * the equally driver-agnostic {@link ResultSet#getString}, which returns the column's JSON text.
 */
@Repository
public class CompRepository
{
    private final JdbcClient jdbcClient;
    private final ObjectMapper objectMapper;

    public CompRepository(JdbcClient jdbcClient, ObjectMapper objectMapper)
    {
        this.jdbcClient = jdbcClient;
        this.objectMapper = objectMapper;
    }

    public Comp save(Comp comp)
    {
        comp.setCreatedAt(Instant.now());
        return jdbcClient.sql("""
                INSERT INTO comps (user_id, placements, created_at)
                VALUES (:userId, :placements, :createdAt)
                RETURNING *
                """)
                .param("userId", comp.getUserId())
                .param("placements", toJson(comp.getPlacements()), Types.OTHER)
                .param("createdAt", Timestamp.from(comp.getCreatedAt()))
                .query(this::mapRow)
                .single();
    }

    public int countByUserId(UUID userId)
    {
        return jdbcClient.sql("SELECT COUNT(*) FROM comps WHERE user_id = :userId")
                .param("userId", userId)
                .query(Integer.class)
                .single();
    }

    public List<Comp> findByUserIdOrderByCreatedAtDesc(UUID userId)
    {
        return jdbcClient.sql("SELECT * FROM comps WHERE user_id = :userId ORDER BY created_at DESC")
                .param("userId", userId)
                .query(this::mapRow)
                .list();
    }

    public Optional<Comp> findByIdAndUserId(UUID id, UUID userId)
    {
        return jdbcClient.sql("SELECT * FROM comps WHERE id = :id AND user_id = :userId")
                .param("id", id)
                .param("userId", userId)
                .query(this::mapRow)
                .optional();
    }

    /**
     * @return true if a row owned by {@code userId} was found and deleted
     */
    public boolean deleteByIdAndUserId(UUID id, UUID userId)
    {
        int rowsDeleted = jdbcClient.sql("DELETE FROM comps WHERE id = :id AND user_id = :userId")
                .param("id", id)
                .param("userId", userId)
                .update();
        return rowsDeleted > 0;
    }

    private Comp mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        Comp comp = new Comp();
        comp.setId(rs.getObject("id", UUID.class));
        comp.setUserId(rs.getObject("user_id", UUID.class));
        comp.setPlacements(fromJson(rs.getString("placements")));
        comp.setCreatedAt(rs.getObject("created_at", OffsetDateTime.class).toInstant());
        return comp;
    }

    private String toJson(List<Placement> placements)
    {
        try
        {
            return objectMapper.writeValueAsString(placements);
        }
        catch (JsonProcessingException e)
        {
            throw new IllegalStateException("Failed to serialize comp placements", e);
        }
    }

    private List<Placement> fromJson(String json)
    {
        try
        {
            return objectMapper.readValue(json, new TypeReference<List<Placement>>() {});
        }
        catch (JsonProcessingException e)
        {
            throw new IllegalStateException("Failed to deserialize comp placements", e);
        }
    }
}
