package com.tfttools.auth.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Reads a Bearer token, and if valid, authenticates the request with the
 * encoded user id as principal. Leaves the request anonymous otherwise -
 * downstream authorization (SecurityFilterChainConfig) decides what that means.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter
{
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider)
    {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                     @NonNull HttpServletResponse response,
                                     @NonNull FilterChain filterChain)
            throws ServletException, IOException
    {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith(BEARER_PREFIX))
        {
            String token = header.substring(BEARER_PREFIX.length());
            Long userId = jwtTokenProvider.validateAndGetUserId(token);
            if (userId != null)
            {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userId, null, List.of());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}
