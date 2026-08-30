package com.tfttools.comps.controller;

import com.tfttools.comps.dto.CompResponse;
import com.tfttools.comps.dto.SaveCompRequest;
import com.tfttools.comps.service.CompService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * All endpoints here require JWT authentication (enforced by {@code SecurityFilterChainConfig}'s
 * {@code /comps/**} rule) - {@code Authentication.getPrincipal()} gives the {@code UUID userId}
 * directly, same as {@code AuthController}.
 */
@RestController
@RequestMapping("/comps")
public class CompController
{
    private final CompService compService;

    public CompController(CompService compService)
    {
        this.compService = compService;
    }

    @PostMapping
    public ResponseEntity<CompResponse> save(@Valid @RequestBody SaveCompRequest request, Authentication authentication)
    {
        UUID userId = (UUID) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED).body(compService.save(userId, request));
    }

    @GetMapping
    public List<CompResponse> list(Authentication authentication)
    {
        UUID userId = (UUID) authentication.getPrincipal();
        return compService.list(userId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id, Authentication authentication)
    {
        UUID userId = (UUID) authentication.getPrincipal();
        compService.delete(userId, id);
        return ResponseEntity.noContent().build();
    }
}
