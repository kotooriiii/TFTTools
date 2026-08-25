package com.tfttools.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfttools.auth.dto.AuthResponse;
import com.tfttools.auth.dto.LoginRequest;
import com.tfttools.auth.dto.OAuthLoginRequest;
import com.tfttools.auth.dto.SignupRequest;
import com.tfttools.auth.dto.UserResponse;
import com.tfttools.auth.exception.DuplicateEmailException;
import com.tfttools.auth.exception.InvalidCredentialsException;
import com.tfttools.auth.exception.UnsupportedOAuthProviderException;
import com.tfttools.auth.security.JwtTokenProvider;
import com.tfttools.auth.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    // JwtAuthenticationFilter is picked up by @WebMvcTest's component scan (it's a servlet Filter)
    // and needs this to construct, even though addFilters = false means it never actually runs here.
    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void signup_validRequest_returns201WithUser() throws Exception
    {
        SignupRequest request = new SignupRequest("newuser", "new@example.com", "password123");
        UserResponse expected = new UserResponse(1L, "newuser", "new@example.com", Instant.now());
        when(authService.register(any())).thenReturn(expected);

        mockMvc.perform(post("/auth/signup")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("newuser"))
                .andExpect(jsonPath("$.email").value("new@example.com"));
    }

    @Test
    void signup_invalidRequest_returns400WithFieldErrors() throws Exception
    {
        SignupRequest invalid = new SignupRequest("", "not-an-email", "short");

        mockMvc.perform(post("/auth/signup")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.password").exists());
    }

    @Test
    void signup_duplicateEmail_returns409() throws Exception
    {
        SignupRequest request = new SignupRequest("newuser", "taken@example.com", "password123");
        when(authService.register(any())).thenThrow(new DuplicateEmailException("An account with this email already exists"));

        mockMvc.perform(post("/auth/signup")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("An account with this email already exists"));
    }

    @Test
    void login_validCredentials_returns200WithToken() throws Exception
    {
        LoginRequest request = new LoginRequest("existing@example.com", "correct-password");
        UserResponse user = new UserResponse(1L, "existing", "existing@example.com", Instant.now());
        when(authService.login(any())).thenReturn(new AuthResponse("signed-jwt", user));

        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("signed-jwt"));
    }

    @Test
    void login_invalidCredentials_returns401() throws Exception
    {
        LoginRequest request = new LoginRequest("existing@example.com", "wrong-password");
        when(authService.login(any())).thenThrow(new InvalidCredentialsException("Invalid email or password"));

        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid email or password"));
    }

    @Test
    void oauthLogin_validCode_returns200WithToken() throws Exception
    {
        OAuthLoginRequest request = new OAuthLoginRequest("auth-code");
        UserResponse user = new UserResponse(3L, "googleuser", "googleuser@example.com", Instant.now());
        when(authService.loginWithOAuth("google", "auth-code")).thenReturn(new AuthResponse("signed-jwt", user));

        mockMvc.perform(post("/auth/oauth/google")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("signed-jwt"));
    }

    @Test
    void oauthLogin_unsupportedProvider_returns400() throws Exception
    {
        OAuthLoginRequest request = new OAuthLoginRequest("auth-code");
        when(authService.loginWithOAuth("bogus", "auth-code"))
                .thenThrow(new UnsupportedOAuthProviderException("Unknown OAuth provider: bogus"));

        mockMvc.perform(post("/auth/oauth/bogus")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Unknown OAuth provider: bogus"));
    }

    @Test
    void me_withAuthenticatedPrincipal_returns200WithUser() throws Exception
    {
        UserResponse expected = new UserResponse(42L, "existing", "existing@example.com", Instant.now());
        when(authService.getCurrentUser(42L)).thenReturn(expected);
        Authentication authentication = new UsernamePasswordAuthenticationToken(42L, null, List.of());

        mockMvc.perform(get("/auth/me").principal(authentication))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(42))
                .andExpect(jsonPath("$.username").value("existing"));
    }
}
