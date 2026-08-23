package com.tfttools.auth;

import com.tfttools.auth.dto.AuthResponse;
import com.tfttools.auth.dto.LoginRequest;
import com.tfttools.auth.dto.SignupRequest;
import com.tfttools.auth.dto.UserResponse;
import com.tfttools.support.AbstractPostgresIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Full signup -> login -> me flow against a real (Testcontainers) Postgres instance and the
 * real Spring Security filter chain - the pieces AuthServiceTest/AuthControllerTest deliberately
 * mock out.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthIntegrationTest extends AbstractPostgresIntegrationTest
{
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void signup_thenDuplicateSignup_thenLogin_thenMe()
    {
        SignupRequest signupRequest = new SignupRequest("integrationuser", "integration@example.com", "password123");

        ResponseEntity<String> signupResponse = restTemplate.postForEntity("/auth/signup", signupRequest, String.class);
        assertThat(signupResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(signupResponse.getBody()).contains("integrationuser").doesNotContain("passwordHash").doesNotContain("password123");

        ResponseEntity<Map> duplicateResponse = restTemplate.postForEntity("/auth/signup", signupRequest, Map.class);
        assertThat(duplicateResponse.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);

        LoginRequest loginRequest = new LoginRequest("integration@example.com", "password123");
        ResponseEntity<AuthResponse> loginResponse = restTemplate.postForEntity("/auth/login", loginRequest, AuthResponse.class);
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        String token = loginResponse.getBody().token();
        assertThat(token).isNotBlank();

        HttpHeaders authorizedHeaders = new HttpHeaders();
        authorizedHeaders.setBearerAuth(token);
        ResponseEntity<UserResponse> meResponse = restTemplate.exchange(
                "/auth/me", org.springframework.http.HttpMethod.GET, new HttpEntity<>(authorizedHeaders), UserResponse.class);
        assertThat(meResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(meResponse.getBody().email()).isEqualTo("integration@example.com");
    }

    @Test
    void login_withWrongPassword_returns401()
    {
        SignupRequest signupRequest = new SignupRequest("wrongpassuser", "wrongpass@example.com", "correct-password");
        restTemplate.postForEntity("/auth/signup", signupRequest, String.class);

        ResponseEntity<Map> loginResponse = restTemplate.postForEntity(
                "/auth/login", new LoginRequest("wrongpass@example.com", "incorrect-password"), Map.class);

        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void me_withoutToken_returns401WithCustomBody()
    {
        ResponseEntity<Map> response = restTemplate.getForEntity("/auth/me", Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).containsEntry("error", "Unauthorized");
    }

    @Test
    void me_withGarbageToken_returns401()
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("this-is-not-a-real-jwt");

        ResponseEntity<Map> response = restTemplate.exchange(
                "/auth/me", org.springframework.http.HttpMethod.GET, new HttpEntity<>(headers), Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void existingToolEndpoint_remainsAccessibleWithoutToken()
    {
        ResponseEntity<String> response = restTemplate.getForEntity("/units", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
