package com.tfttools.auth.controller;

import com.tfttools.auth.dto.AuthResponse;
import com.tfttools.auth.dto.LoginRequest;
import com.tfttools.auth.dto.OAuthLoginRequest;
import com.tfttools.auth.dto.SignupRequest;
import com.tfttools.auth.dto.UserResponse;
import com.tfttools.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController
{
    private final AuthService authService;

    public AuthController(AuthService authService)
    {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@Valid @RequestBody SignupRequest request)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request)
    {
        return authService.login(request);
    }

    @PostMapping("/oauth/{provider}")
    public AuthResponse oauthLogin(@PathVariable String provider, @Valid @RequestBody OAuthLoginRequest request)
    {
        return authService.loginWithOAuth(provider, request.code());
    }

    @GetMapping("/me")
    public UserResponse me(Authentication authentication)
    {
        Long userId = (Long) authentication.getPrincipal();
        return authService.getCurrentUser(userId);
    }
}
