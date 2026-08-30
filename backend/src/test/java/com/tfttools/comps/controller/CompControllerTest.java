package com.tfttools.comps.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfttools.auth.security.JwtTokenProvider;
import com.tfttools.comps.dto.CompResponse;
import com.tfttools.comps.dto.PlacementDTO;
import com.tfttools.comps.dto.SaveCompRequest;
import com.tfttools.comps.exception.CompLimitExceededException;
import com.tfttools.comps.exception.CompNotFoundException;
import com.tfttools.comps.service.CompService;
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
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CompController.class)
@AutoConfigureMockMvc(addFilters = false)
class CompControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CompService compService;

    // JwtAuthenticationFilter is picked up by @WebMvcTest's component scan (it's a servlet Filter)
    // and needs this to construct, even though addFilters = false means it never actually runs here.
    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void save_validRequest_returns201WithComp() throws Exception
    {
        UUID userId = UUID.randomUUID();
        SaveCompRequest request = new SaveCompRequest(List.of(new PlacementDTO("TFT18_Ahri", 1, 3)));
        CompResponse expected = new CompResponse(UUID.randomUUID(), request.placements(), Instant.now());
        when(compService.save(eq(userId), any())).thenReturn(expected);

        mockMvc.perform(post("/comps")
                        .principal(authentication(userId))
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.placements[0].unitApiName").value("TFT18_Ahri"));
    }

    @Test
    void save_emptyPlacements_returns400() throws Exception
    {
        SaveCompRequest invalid = new SaveCompRequest(List.of());

        mockMvc.perform(post("/comps")
                        .principal(authentication(UUID.randomUUID()))
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void save_pastCap_returns409() throws Exception
    {
        UUID userId = UUID.randomUUID();
        SaveCompRequest request = new SaveCompRequest(List.of(new PlacementDTO("TFT18_Ahri", 1, 3)));
        when(compService.save(eq(userId), any()))
                .thenThrow(new CompLimitExceededException("You've reached the limit of 25 saved comps. Delete one before saving another."));

        mockMvc.perform(post("/comps")
                        .principal(authentication(userId))
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void list_returnsUsersComps() throws Exception
    {
        UUID userId = UUID.randomUUID();
        CompResponse comp = new CompResponse(UUID.randomUUID(), List.of(new PlacementDTO("TFT18_Ahri", 1, 3)), Instant.now());
        when(compService.list(userId)).thenReturn(List.of(comp));

        mockMvc.perform(get("/comps").principal(authentication(userId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].placements[0].unitApiName").value("TFT18_Ahri"));
    }

    @Test
    void delete_existingOwnedComp_returns204() throws Exception
    {
        UUID userId = UUID.randomUUID();
        UUID compId = UUID.randomUUID();

        mockMvc.perform(delete("/comps/{id}", compId).principal(authentication(userId)))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_missingOrNotOwnedComp_returns404() throws Exception
    {
        UUID userId = UUID.randomUUID();
        UUID compId = UUID.randomUUID();
        org.mockito.Mockito.doThrow(new CompNotFoundException("Comp not found"))
                .when(compService).delete(userId, compId);

        mockMvc.perform(delete("/comps/{id}", compId).principal(authentication(userId)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Comp not found"));
    }

    private static Authentication authentication(UUID userId)
    {
        return new UsernamePasswordAuthenticationToken(userId, null, List.of());
    }
}
