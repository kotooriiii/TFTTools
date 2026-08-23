package com.tfttools.auth.repository;

import com.tfttools.auth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository backed by Postgres - unrelated to the in-memory,
 * Community-Dragon-backed repositories in {@code com.tfttools.repository}
 * (e.g. TraitRepository, UnitRepository).
 */
public interface UserRepository extends JpaRepository<User, Long>
{
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
