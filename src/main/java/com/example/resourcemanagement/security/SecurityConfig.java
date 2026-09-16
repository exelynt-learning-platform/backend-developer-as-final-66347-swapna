package com.example.resourcemanagement.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            // Disable CSRF because we are using JWT
            .csrf(csrf -> csrf.disable())

            // Don't create HTTP sessions
            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            // Authorization rules
            .authorizeHttpRequests(auth -> auth

                // Public endpoints
                .requestMatchers(
                    "/auth/login",
                    "/auth/register",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/v3/api-docs/**"
                ).permitAll()

                // RESOURCE APIs
                .requestMatchers(
                    HttpMethod.GET,
                    "/resources"
                ).hasAnyRole("USER", "ADMIN")

                .requestMatchers(
                    HttpMethod.POST,
                    "/resources"
                ).hasRole("ADMIN")

                .requestMatchers(
                    HttpMethod.PUT,
                    "/resources/**"
                ).hasRole("ADMIN")

                .requestMatchers(
                    HttpMethod.DELETE,
                    "/resources/**"
                ).hasRole("ADMIN")

                // RESERVATION APIs
                .requestMatchers(
                    HttpMethod.GET,
                    "/reservation"
                ).hasAnyRole("USER", "ADMIN")

                .requestMatchers(
                    HttpMethod.POST,
                    "/reservation"
                ).hasAnyRole("USER", "ADMIN")

                .requestMatchers(
                    HttpMethod.PUT,
                    "/reservation/**"
                ).hasRole("ADMIN")

                .requestMatchers(
                    HttpMethod.DELETE,
                    "/reservation/**"
                ).hasRole("ADMIN")

                .requestMatchers(
                    HttpMethod.PATCH,
                    "/reservation/**"
                ).hasRole("ADMIN")

                // Everything else requires authentication
                .anyRequest().authenticated()
            )

            // JWT filter
            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}