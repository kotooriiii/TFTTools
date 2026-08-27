package com.tfttools.auth.service;

import com.tfttools.auth.domain.User;
import com.tfttools.auth.dto.AuthResponse;
import com.tfttools.auth.dto.LoginRequest;
import com.tfttools.auth.dto.SignupRequest;
import com.tfttools.auth.dto.UserResponse;
import com.tfttools.auth.exception.DuplicateEmailException;
import com.tfttools.auth.exception.InvalidCredentialsException;
import com.tfttools.auth.exception.UnsupportedOAuthProviderException;
import com.tfttools.auth.oauth.OAuthProvider;
import com.tfttools.auth.oauth.OAuthUserInfo;
import com.tfttools.auth.exception.OAuthAuthenticationException;
import com.tfttools.auth.repository.UserRepository;
import com.tfttools.auth.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AuthService
{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final Map<String, OAuthProvider> oauthProvidersByKey;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider,
                        List<OAuthProvider> oauthProviders)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.oauthProvidersByKey = oauthProviders.stream()
                .collect(Collectors.toMap(OAuthProvider::getProviderKey, Function.identity()));
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

        if (user.getPasswordHash() == null)
        {
            throw new InvalidCredentialsException("This account uses Google sign-in — continue with Google below");
        }

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash()))
        {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String token = jwtTokenProvider.generateToken(user.getId());
        return new AuthResponse(token, UserResponse.from(user));
    }

    public AuthResponse loginWithOAuth(String providerKey, String code)
    {
        OAuthProvider provider = oauthProvidersByKey.get(providerKey);
        if (provider == null)
        {
            throw new UnsupportedOAuthProviderException("Unknown OAuth provider: " + providerKey);
        }

        OAuthUserInfo info = provider.exchangeCodeForUserInfo(code);
        if (!info.emailVerified())
        {
            throw new OAuthAuthenticationException("Google account email is not verified");
        }

        User user = userRepository.findByOauthProviderAndOauthSubjectId(providerKey, info.subjectId())
                .orElseGet(() -> userRepository.findByEmail(info.email())
                        .map(existing -> linkOAuthIdentity(existing, providerKey, info))
                        .orElseGet(() -> createOAuthUser(providerKey, info)));

        String token = jwtTokenProvider.generateToken(user.getId());
        return new AuthResponse(token, UserResponse.from(user));
    }

    private User linkOAuthIdentity(User user, String providerKey, OAuthUserInfo info)
    {
        user.setOauthProvider(providerKey);
        user.setOauthSubjectId(info.subjectId());
        return userRepository.save(user);
    }

    private User createOAuthUser(String providerKey, OAuthUserInfo info)
    {
        User user = new User();
        user.setUsername(info.displayName() != null ? info.displayName() : info.email());
        user.setEmail(info.email());
        user.setOauthProvider(providerKey);
        user.setOauthSubjectId(info.subjectId());
        user.setCreatedAt(Instant.now());
        return userRepository.save(user);
    }

    public UserResponse getCurrentUser(UUID userId)
    {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidCredentialsException("User no longer exists"));
        return UserResponse.from(user);
    }
}
