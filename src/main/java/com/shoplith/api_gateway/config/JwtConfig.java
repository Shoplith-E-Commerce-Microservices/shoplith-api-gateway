package com.shoplith.api_gateway.config;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Configuration

public class JwtConfig {

    @Value("${jwt.secret}")

    private String secret;

    @Bean

    public ReactiveJwtDecoder reactiveJwtDecoder() {

        SecretKey key = Keys.hmacShaKeyFor(

                secret.getBytes(StandardCharsets.UTF_8)

        );

        return NimbusReactiveJwtDecoder

                .withSecretKey(key)

                .build();

    }

}