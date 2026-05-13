package com.shoplith.api_gateway.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.security.core.AuthenticationException;

import org.springframework.security.web.server
        .ServerAuthenticationEntryPoint;

import org.springframework.stereotype.Component;

import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class CustomAuthenticationEntryPoint
        implements ServerAuthenticationEntryPoint {

    @Override
    public Mono<Void> commence(
            ServerWebExchange exchange,
            AuthenticationException ex
    ) {

        var response = exchange.getResponse();

        response.setStatusCode(
                HttpStatus.UNAUTHORIZED
        );

        response.getHeaders().setContentType(
                MediaType.APPLICATION_JSON
        );

        String body = """
                {
                  "code": 401,
                  "status": "ERROR",
                  "message": "Unauthorized Access",
                  "data": null
                }
                """;

        var buffer = response.bufferFactory()
                .wrap(
                        body.getBytes(
                                StandardCharsets.UTF_8
                        )
                );

        return response.writeWith(
                Mono.just(buffer)
        );
    }
}