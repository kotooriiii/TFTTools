package com.tfttools.auth.repository;

import com.tfttools.auth.domain.User;
import com.tfttools.support.AbstractPostgresIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(UserRepository.class)
class UserRepositoryTest extends AbstractPostgresIntegrationTest
{
    @Autowired
    private UserRepository userRepository;

    @Test
    void findByEmail_returnsUserWhenPresent()
    {
        User user = persistUser("present@example.com");

        assertThat(userRepository.findByEmail("present@example.com"))
                .hasValueSatisfying(found -> assertThat(found.getId()).isEqualTo(user.getId()));
    }

    @Test
    void findByEmail_returnsEmptyWhenAbsent()
    {
        assertThat(userRepository.findByEmail("absent@example.com")).isEmpty();
    }

    @Test
    void existsByEmail_reflectsPresenceOfEmail()
    {
        persistUser("exists@example.com");

        assertThat(userRepository.existsByEmail("exists@example.com")).isTrue();
        assertThat(userRepository.existsByEmail("missing@example.com")).isFalse();
    }

    @Test
    void save_rejectsDuplicateEmail()
    {
        persistUser("duplicate@example.com");

        User secondUser = new User();
        secondUser.setUsername("second");
        secondUser.setEmail("duplicate@example.com");
        secondUser.setPasswordHash("hash");

        assertThatThrownBy(() -> userRepository.save(secondUser))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    private User persistUser(String email)
    {
        User user = new User();
        user.setUsername("user-" + email);
        user.setEmail(email);
        user.setPasswordHash("hash");
        user.setCreatedAt(Instant.now());
        return userRepository.save(user);
    }
}
