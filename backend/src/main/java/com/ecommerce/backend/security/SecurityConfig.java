package com.ecommerce.backend.security;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(
            CustomUserDetailsService userDetailsService
    ) {
        this.userDetailsService =
                userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        CookieCsrfTokenRepository csrfTokenRepository =
                CookieCsrfTokenRepository.withHttpOnlyFalse();

        CsrfTokenRequestAttributeHandler csrfRequestHandler =
                new CsrfTokenRequestAttributeHandler();

        http

            // ==========================================
            // CORS
            // ==========================================

            .cors(cors ->
                    cors.configurationSource(
                            corsConfigurationSource()
                    )
            )

            // ==========================================
            // CSRF
            // ==========================================

            .csrf(csrf ->
                    csrf
                        .csrfTokenRepository(
                                csrfTokenRepository
                        )
                        .csrfTokenRequestHandler(
                                csrfRequestHandler
                        )
            )

            // ==========================================
            // SESSION
            // ==========================================

            .sessionManagement(session ->
                    session.sessionCreationPolicy(
                            SessionCreationPolicy.IF_REQUIRED
                    )
            )

            // ==========================================
            // EXCEPTION HANDLING
            // ==========================================

            .exceptionHandling(exception ->
                    exception.authenticationEntryPoint(
                            restAuthenticationEntryPoint()
                    )
            )

            // ==========================================
            // AUTHORIZATION
            // ==========================================

            .authorizeHttpRequests(auth -> {

                // --------------------------------------
                // Public Authentication APIs
                // --------------------------------------

                auth.requestMatchers(
                        "/api/auth/login",
                        "/api/auth/register",
                        "/api/auth/csrf"
                ).permitAll();

                // --------------------------------------
                // Authenticated Customer APIs
                // --------------------------------------

                auth.requestMatchers(
                        "/api/auth/me"
                ).authenticated();

                auth.requestMatchers(
                        "/api/auth/logout"
                ).authenticated();

                // --------------------------------------
                // Public Product APIs
                // --------------------------------------

                auth.requestMatchers(
                        "/api/products/**"
                ).permitAll();

                // --------------------------------------
                // ADMIN ONLY APIs
                // --------------------------------------
                //
                // Only users having ROLE_ADMIN
                // can access these endpoints.
                //
                // CUSTOMER -> 403 Forbidden
                // ADMIN    -> allowed
                //
                // --------------------------------------

                auth.requestMatchers(
                        "/api/admin/**"
                ).hasRole("ADMIN");

                // --------------------------------------
                // Customer Order APIs
                // --------------------------------------

                auth.requestMatchers(
                        "/api/orders/**"
                ).authenticated();

                // --------------------------------------
                // Other APIs
                // --------------------------------------

                auth.anyRequest().permitAll();
            });

        return http.build();
    }

    // ==========================================
    // CORS Configuration
    // ==========================================

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        configuration.setAllowedOrigins(
                java.util.List.of(
                        "http://localhost:3000"
                )
        );

        configuration.setAllowCredentials(
                true
        );

        configuration.setAllowedMethods(
                java.util.List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "PATCH",
                        "DELETE",
                        "OPTIONS"
                )
        );

        configuration.setAllowedHeaders(
                java.util.List.of(
                        "Content-Type",
                        "X-XSRF-TOKEN",
                        "X-Requested-With",
                        "Accept",
                        "Origin"
                )
        );

        configuration.setExposedHeaders(
                java.util.List.of(
                        "Set-Cookie"
                )
        );

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }

    // ==========================================
    // Authentication Entry Point
    // ==========================================

    @Bean
    public AuthenticationEntryPoint
    restAuthenticationEntryPoint() {

        return (
                request,
                response,
                exception
        ) -> response.sendError(
                HttpServletResponse.SC_UNAUTHORIZED,
                "Authentication required."
        );
    }

    // ==========================================
    // Authentication Manager
    // ==========================================

    @Bean
    public AuthenticationManager
    authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {

        return configuration.getAuthenticationManager();
    }

    // ==========================================
    // Authentication Provider
    // ==========================================

    @Bean
    public DaoAuthenticationProvider
    authenticationProvider() {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(
                        userDetailsService
                );

        provider.setPasswordEncoder(
                passwordEncoder()
        );

        return provider;
    }

    // ==========================================
    // Password Encoder
    // ==========================================

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }
}