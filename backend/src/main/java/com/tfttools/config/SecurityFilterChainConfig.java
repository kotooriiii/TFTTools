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
import org.springframework.security.web.SecurityFilterChain;
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
                    // behavior). Spring Security treats an unmatched request as implicitly allowed,
                    // which is why e.g. /cdragon/** already works with no entry below. Any path that
                    // must require auth (like /auth/me) has to be listed explicitly with .authenticated().
                    authorizationManagerRequestMatcherRegistry
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
                .exceptionHandling(exceptionHandlingConfigurer -> exceptionHandlingConfigurer
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

}