package com.tfttools.config;


import com.tfttools.auth.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityFilterChainConfig
{
    private final CorsConfigurationSource corsConfigurationSource;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    /**
     * Default Spring Security behavior (403 via Http403ForbiddenEntryPoint) everywhere except
     * /auth/**, which returns 401 as required by the auth endpoints' acceptance criteria.
     */
    private AuthenticationEntryPoint authAuthenticationEntryPoint()
    {
        Http403ForbiddenEntryPoint defaultEntryPoint = new Http403ForbiddenEntryPoint();
        return (request, response, authException) ->
        {
            if (request.getRequestURI().startsWith("/auth/"))
            {
                // Write directly rather than response.sendError(...): sendError triggers Tomcat's
                // error-page forward to /error, which re-enters this same filter chain, gets denied
                // again (no rule permits /error), and re-invokes this entry point for that second
                // pass - overwriting the intended 401 with the default entry point's 403.
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"Unauthorized\"}");
            }
            else
            {
                defaultEntryPoint.commence(request, response, authException);
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception
    {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource))
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                {
                    // NOTE: there is deliberately no trailing anyRequest() rule here (pre-existing
                    // behavior, unchanged). Verified empirically that unmatched requests are denied
                    // (403) under the current Spring Security version - e.g. GET /cdragon/cache/status
                    // and GET /units/traits already return 403 today with no auth changes at all. Not
                    // touching that pre-existing gap here; only /auth/** rules (and /error, below) are new.
                    authorizationManagerRequestMatcherRegistry
                            // /error must be explicitly permitted (for all methods - a forward preserves
                            // the original request's HTTP method). Any unhandled exception in ANY
                            // controller forwards here, and that forward re-enters this same filter
                            // chain as a new request; without this, an unmatched/denied /error pass
                            // silently overwrites the real status (e.g. a 404 or 500) with a 403. This
                            // was already happening on master before this ticket - not new behavior,
                            // just previously-undiscovered because nothing permitted /error.
                            .requestMatchers("/error")
                            .permitAll()
                            .requestMatchers(
                                    HttpMethod.GET,
                                    "/units/filter",
                                    "/units/search/**",
                                    "/units/traits",
                                    "/units",
                                    "/tools/*"
                            ).permitAll().requestMatchers(
                                    HttpMethod.POST,
                                    "/tools/horizontal"
                            ).permitAll().requestMatchers(
                                    HttpMethod.POST,
                                    "/auth/signup",
                                    "/auth/login"
                            ).permitAll().requestMatchers(
                                    HttpMethod.GET,
                                    "/auth/me"
                            ).authenticated();
                })
                // Scoped to /auth/** only, so every other (pre-existing) denied path keeps returning
                // the default 403 it already returned before this ticket - only /auth/me needs the
                // clean 401 required by its acceptance criteria.
                .exceptionHandling(exceptionHandlingConfigurer -> exceptionHandlingConfigurer
                        .authenticationEntryPoint(authAuthenticationEntryPoint()))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

}