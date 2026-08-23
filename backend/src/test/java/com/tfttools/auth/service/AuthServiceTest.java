package com.tfttools.auth.service;

import com.tfttools.auth.domain.User;
import com.tfttools.auth.dto.AuthResponse;
import com.tfttools.auth.dto.LoginRequest;
import com.tfttools.auth.dto.SignupRequest;
import com.tfttools.auth.dto.UserResponse;
import com.tfttools.auth.exception.DuplicateEmailException;
import com.tfttools.auth.exception.InvalidCredentialsException;
import com.tfttools.auth.repository.UserRepository;
import com.tfttools.auth.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest
{
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private AuthService authService;

    @org.junit.jupiter.api.BeforeEach
    void setUp()
    {
        authService = new AuthService(userRepository, passwordEncoder, jwtTokenProvider);
    }

    @Test
    void register_savesUserWithEncodedPassword()
    {
        SignupRequest request = new SignupRequest("newuser", "new@example.com", "password123");
        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn("encoded-hash");
        when(userRepository.save(any(User.class))).thenAnswer(invocation ->
        {
            User u = invocation.getArgument(0);
            u.setId(1L);
            return u;
        });

        UserResponse response = authService.register(request);

        assertThat(response.username()).isEqualTo("newuser");
        assertThat(response.email()).isEqualTo("new@example.com");

        ArgumentCaptor<User> savedUser = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(savedUser.capture());
        assertThat(savedUser.getValue().getPasswordHash()).isEqualTo("encoded-hash");
    }

    @Test
    void register_throwsOnDuplicateEmail_andNeverSaves()
    {
        SignupRequest request = new SignupRequest("newuser", "taken@example.com", "password123");
        when(userRepository.existsByEmail(request.email())).thenReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(DuplicateEmailException.class);

        verify(userRepository, never()).save(any());
    }

    @Test
    void login_returnsTokenForValidCredentials()
    {
        User user = existingUser();
        LoginRequest request = new LoginRequest(user.getEmail(), "correct-password");
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("correct-password", user.getPasswordHash())).thenReturn(true);
        when(jwtTokenProvider.generateToken(user.getId())).thenReturn("signed-jwt");

        AuthResponse response = authService.login(request);

        assertThat(response.token()).isEqualTo("signed-jwt");
        assertThat(response.user().email()).isEqualTo(user.getEmail());
    }

    @Test
    void login_throwsSameMessageForUnknownEmail_asForWrongPassword()
    {
        User user = existingUser();

        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());
        String unknownEmailMessage = catchMessage(() ->
                authService.login(new LoginRequest("unknown@example.com", "whatever")));

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), eq(user.getPasswordHash()))).thenReturn(false);
        String wrongPasswordMessage = catchMessage(() ->
                authService.login(new LoginRequest(user.getEmail(), "wrong-password")));

        assertThat(unknownEmailMessage).isEqualTo(wrongPasswordMessage);
    }

    @Test
    void getCurrentUser_returnsUserWhenFound()
    {
        User user = existingUser();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        UserResponse response = authService.getCurrentUser(user.getId());

        assertThat(response.id()).isEqualTo(user.getId());
    }

    @Test
    void getCurrentUser_throwsWhenUserNoLongerExists()
    {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.getCurrentUser(99L))
                .isInstanceOf(InvalidCredentialsException.class);
    }

    private static User existingUser()
    {
        User user = new User();
        user.setId(7L);
        user.setUsername("existing");
        user.setEmail("existing@example.com");
        user.setPasswordHash("hashed-password");
        user.setCreatedAt(Instant.now());
        return user;
    }

    private static String catchMessage(Runnable runnable)
    {
        try
        {
            runnable.run();
            throw new AssertionError("Expected InvalidCredentialsException but none was thrown");
        }
        catch (InvalidCredentialsException e)
        {
            return e.getMessage();
        }
    }
}
