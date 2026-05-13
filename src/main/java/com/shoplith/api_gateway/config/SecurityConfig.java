package com.shoplith.api_gateway.config;

import com.shoplith.api_gateway.handler
        .CustomAccessDeniedHandler;

import com.shoplith.api_gateway.handler
        .CustomAuthenticationEntryPoint;

import io.jsonwebtoken.security.Keys;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.reactive
        .EnableWebFluxSecurity;

import org.springframework.security.config.web.server
        .ServerHttpSecurity;

import org.springframework.security.oauth2.jwt
        .NimbusReactiveJwtDecoder;

import org.springframework.security.oauth2.jwt
        .ReactiveJwtDecoder;

import org.springframework.security.web.server
        .SecurityWebFilterChain;

import javax.crypto.SecretKey;

import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${jwt.secret}")
    private String secret;

    private final CustomAuthenticationEntryPoint
            authenticationEntryPoint;

    private final CustomAccessDeniedHandler
            customAccessDeniedHandler;

    @Bean
    public SecurityWebFilterChain securityFilterChain(
            ServerHttpSecurity http
    ) {

        return http

                .csrf(ServerHttpSecurity
                        .CsrfSpec::disable)

                .authorizeExchange(exchange -> exchange

                        .pathMatchers(
                                "/api/v1/auth/**"
                        ).permitAll()

                        .anyExchange()
                        .authenticated()
                )

                .oauth2ResourceServer(oauth2 ->

                        oauth2

                                .authenticationEntryPoint(
                                        authenticationEntryPoint
                                )

                                .accessDeniedHandler(
                                        customAccessDeniedHandler
                                )

                                .jwt(jwt -> {})
                )

                .build();
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {

        SecretKey key = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );

        return NimbusReactiveJwtDecoder
                .withSecretKey(key)
                .build();
    }
}