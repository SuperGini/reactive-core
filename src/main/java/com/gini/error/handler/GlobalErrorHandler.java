package com.gini.error.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gini.error.ErrorResponse;
import com.gini.exceptions.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

//https://medium.com/@akhil.bojedla/exception-handling-spring-webflux-b11647d8608
@Slf4j
@Component
@RequiredArgsConstructor
public class GlobalErrorHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {

        DataBufferFactory dataBufferFactory = exchange.getResponse().bufferFactory();
        DataBuffer errorResponse;

        if(ex instanceof CustomerNotFoundException){
            log.debug("Error message: {}", ex.getMessage());
            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
            exchange.getResponse().getHeaders().setContentType(MediaType.TEXT_EVENT_STREAM);

            try {
                errorResponse = dataBufferFactory.wrap(objectMapper.writeValueAsBytes(
                        new ErrorResponse(
                                HttpStatus.BAD_REQUEST.value(),
                                ex.getMessage()
                        )
                ));
            } catch (JsonProcessingException e) {
                exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                errorResponse = dataBufferFactory.wrap("Unknown error".getBytes());
            }

            return exchange.getResponse().writeWith(Mono.just(errorResponse));
        }

        log.debug("Error message: {}", ex.getMessage());
        exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        errorResponse = dataBufferFactory.wrap("Unknown error".getBytes());
        return exchange.getResponse().writeWith(Mono.just(errorResponse));
    }
}
