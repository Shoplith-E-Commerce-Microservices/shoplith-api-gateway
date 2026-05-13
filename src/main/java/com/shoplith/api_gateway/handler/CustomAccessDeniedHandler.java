package com.shoplith.api_gateway.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.security.access.AccessDeniedException;

import org.springframework.security.web.server.authorization
        .ServerAccessDeniedHandler;

import org.springframework.stereotype.Component;

import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class CustomAccessDeniedHandler
        implements ServerAccessDeniedHandler {

    @Override
    public Mono<Void> handle(
            ServerWebExchange exchange,
            AccessDeniedException denied
    ) {

        var response = exchange.getResponse();

        response.setStatusCode(
                HttpStatus.FORBIDDEN
        );

        response.getHeaders().setContentType(
                MediaType.APPLICATION_JSON
        );

        String body = """
                {
                  "code": 403,
                  "status": "ERROR",
                  "message": "Access Denied",
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