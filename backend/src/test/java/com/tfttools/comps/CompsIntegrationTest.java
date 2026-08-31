package com.tfttools.comps;

import com.tfttools.auth.dto.AuthResponse;
import com.tfttools.auth.dto.SignupRequest;
import com.tfttools.comps.dto.CompResponse;
import com.tfttools.comps.dto.PlacementDTO;
import com.tfttools.comps.dto.SaveCompRequest;
import com.tfttools.support.AbstractPostgresIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Full save -> list -> delete flow against a real (Testcontainers) Postgres instance and the
 * real Spring Security filter chain, mirroring {@code auth.AuthIntegrationTest}.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CompsIntegrationTest extends AbstractPostgresIntegrationTest
{
    @Autowired
    private TestRestTemplate restTemplate;

    private String signUpAndLogIn(String username, String email)
    {
        SignupRequest signupRequest = new SignupRequest(username, email, "password123");
        restTemplate.postForEntity("/auth/signup", signupRequest, String.class);

        ResponseEntity<AuthResponse> loginResponse = restTemplate.postForEntity(
                "/auth/login", new com.tfttools.auth.dto.LoginRequest(email, "password123"), AuthResponse.class);
        return loginResponse.getBody().token();
    }

    private HttpHeaders authorizedHeaders(String token)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        return headers;
    }

    @Test
    void save_thenList_thenDelete_fullFlow()
    {
        String token = signUpAndLogIn("compsuser", "comps@example.com");
        SaveCompRequest request = new SaveCompRequest(List.of(new PlacementDTO("TFT18_Ahri", 1, 3)));

        ResponseEntity<CompResponse> saveResponse = restTemplate.exchange(
                "/comps", HttpMethod.POST, new HttpEntity<>(request, authorizedHeaders(token)), CompResponse.class);
        assertThat(saveResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        UUID savedId = saveResponse.getBody().id();
        assertThat(saveResponse.getBody().placements()).hasSize(1);
        assertThat(saveResponse.getBody().placements().get(0).unitApiName()).isEqualTo("TFT18_Ahri");

        ResponseEntity<CompResponse[]> listResponse = restTemplate.exchange(
                "/comps", HttpMethod.GET, new HttpEntity<>(authorizedHeaders(token)), CompResponse[].class);
        assertThat(listResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(listResponse.getBody()).extracting(CompResponse::id).contains(savedId);

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "/comps/" + savedId, HttpMethod.DELETE, new HttpEntity<>(authorizedHeaders(token)), Void.class);
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<CompResponse[]> afterDeleteResponse = restTemplate.exchange(
                "/comps", HttpMethod.GET, new HttpEntity<>(authorizedHeaders(token)), CompResponse[].class);
        assertThat(afterDeleteResponse.getBody()).extracting(CompResponse::id).doesNotContain(savedId);
    }

    @Test
    void save_samePlacementUnitSet_overwritesExistingComp()
    {
        String token = signUpAndLogIn("overwriteuser", "overwrite@example.com");
        HttpHeaders headers = authorizedHeaders(token);

        SaveCompRequest first = new SaveCompRequest(List.of(
                new PlacementDTO("TFT18_Ahri", 1, 3), new PlacementDTO("TFT18_Jinx", 0, 0)));
        ResponseEntity<CompResponse> firstResponse = restTemplate.exchange(
                "/comps", HttpMethod.POST, new HttpEntity<>(first, headers), CompResponse.class);
        assertThat(firstResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        UUID savedId = firstResponse.getBody().id();

        // Same two units, different board positions and placement order
        SaveCompRequest moved = new SaveCompRequest(List.of(
                new PlacementDTO("TFT18_Jinx", 2, 5), new PlacementDTO("TFT18_Ahri", 3, 6)));
        ResponseEntity<CompResponse> movedResponse = restTemplate.exchange(
                "/comps", HttpMethod.POST, new HttpEntity<>(moved, headers), CompResponse.class);
        assertThat(movedResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(movedResponse.getBody().id()).isEqualTo(savedId);
        assertThat(movedResponse.getBody().placements()).extracting(PlacementDTO::row).containsExactlyInAnyOrder(2, 3);

        ResponseEntity<CompResponse[]> listResponse = restTemplate.exchange(
                "/comps", HttpMethod.GET, new HttpEntity<>(headers), CompResponse[].class);
        assertThat(listResponse.getBody()).hasSize(1);
        assertThat(listResponse.getBody()[0].id()).isEqualTo(savedId);
    }

    @Test
    void save_pastCap_returns409_andFirst25Unaffected()
    {
        String token = signUpAndLogIn("capuser", "cap@example.com");
        HttpHeaders headers = authorizedHeaders(token);

        // Each save uses a distinct unit set so these are 25 genuinely different comps, not 25
        // same-units-different-position overwrites of one comp (see save_samePlacementUnitSet_overwritesExistingComp).
        for (int i = 0; i < 25; i++)
        {
            SaveCompRequest request = new SaveCompRequest(List.of(new PlacementDTO("TFT18_Unit" + i, 0, i % 7)));
            ResponseEntity<CompResponse> response = restTemplate.exchange(
                    "/comps", HttpMethod.POST, new HttpEntity<>(request, headers), CompResponse.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        }

        SaveCompRequest twentySixth = new SaveCompRequest(List.of(new PlacementDTO("TFT18_Unit25", 1, 0)));
        ResponseEntity<Map> rejectedResponse = restTemplate.exchange(
                "/comps", HttpMethod.POST, new HttpEntity<>(twentySixth, headers), Map.class);
        assertThat(rejectedResponse.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);

        ResponseEntity<CompResponse[]> listResponse = restTemplate.exchange(
                "/comps", HttpMethod.GET, new HttpEntity<>(headers), CompResponse[].class);
        assertThat(listResponse.getBody()).hasSize(25);
    }

    @Test
    void delete_anotherUsersComp_returns404()
    {
        String ownerToken = signUpAndLogIn("owner", "owner@example.com");
        String otherToken = signUpAndLogIn("otheruser", "other@example.com");

        SaveCompRequest request = new SaveCompRequest(List.of(new PlacementDTO("TFT18_Ahri", 1, 3)));
        ResponseEntity<CompResponse> saveResponse = restTemplate.exchange(
                "/comps", HttpMethod.POST, new HttpEntity<>(request, authorizedHeaders(ownerToken)), CompResponse.class);
        UUID savedId = saveResponse.getBody().id();

        ResponseEntity<Map> deleteResponse = restTemplate.exchange(
                "/comps/" + savedId, HttpMethod.DELETE, new HttpEntity<>(authorizedHeaders(otherToken)), Map.class);
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void comps_withoutToken_returns403()
    {
        ResponseEntity<Map> response = restTemplate.getForEntity("/comps", Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
}
