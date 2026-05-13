package com.shoplith.api_gateway.handler;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.shoplith.api_gateway.response.ApiResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.web.reactive.error
        .ErrorWebExceptionHandler;

import org.springframework.core.annotation.Order;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.stereotype.Component;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
@Order(-2)
@RequiredArgsConstructor
public class GlobalErrorHandler
        implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(
            ServerWebExchange exchange,
            Throwable ex
    ) {

        var response = exchange.getResponse();

        response.getHeaders().setContentType(
                MediaType.APPLICATION_JSON
        );

        try {

            // =========================
            // 404 NOT FOUND
            // =========================
            if (ex instanceof ResponseStatusException error &&
                    error.getStatusCode()
                            .equals(HttpStatus.NOT_FOUND)) {

                response.setStatusCode(
                        HttpStatus.NOT_FOUND
                );

                ApiResponse<Object> apiResponse =
                        new ApiResponse<>(
                                404,
                                "No Static Resource Found"+" "+exchange.getRequest().getURI().getPath(),
                                ApiResponse.Status.ERROR
                        );

                String body =
                        objectMapper.writeValueAsString(
                                apiResponse
                        );

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

            // =========================
            // 500 INTERNAL ERROR
            // =========================
            response.setStatusCode(
                    HttpStatus.INTERNAL_SERVER_ERROR
            );

            ApiResponse<Object> apiResponse =
                    new ApiResponse<>(
                            500,
                            "Internal Server Error",
                            ApiResponse.Status.ERROR
                    );

            String body =
                    objectMapper.writeValueAsString(
                            apiResponse
                    );

            var buffer = response.bufferFactory()
                    .wrap(
                            body.getBytes(
                                    StandardCharsets.UTF_8
                            )
                    );

            return response.writeWith(
                    Mono.just(buffer)
            );

        } catch (Exception e) {

            return Mono.error(e);
        }
    }
}