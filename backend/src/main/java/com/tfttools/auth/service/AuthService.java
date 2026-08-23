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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AuthService
{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public UserResponse register(SignupRequest request)
    {
        if (userRepository.existsByEmail(request.email()))
        {
            throw new DuplicateEmailException("An account with this email already exists");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setCreatedAt(Instant.now());

        return UserResponse.from(userRepository.save(user));
    }

    public AuthResponse login(LoginRequest request)
    {
        // Same exception/message for "no such email" and "wrong password" to avoid email enumeration
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash()))
        {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String token = jwtTokenProvider.generateToken(user.getId());
        return new AuthResponse(token, UserResponse.from(user));
    }

    public UserResponse getCurrentUser(Long userId)
    {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidCredentialsException("User no longer exists"));
        return UserResponse.from(user);
    }
}
