package com.gini.error.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gini.exceptions.CustomerAlreadyExistsException;
import com.gini.exceptions.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

//https://medium.com/@akhil.bojedla/exception-handling-spring-webflux-b11647d8608
@Slf4j
@Component
@RequiredArgsConstructor
public class GlobalErrorHandler implements ErrorWebExceptionHandler {

    private static final String DEFAULT_ERROR_MESSAGE = "Unable to process request";

    private final ObjectMapper objectMapper;


    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {


        if(ex instanceof CustomerNotFoundException){
            return mappingErrorResponse(exchange, ex.getMessage(), HttpStatus.NOT_FOUND);
        }

        if(ex instanceof CustomerAlreadyExistsException){
           return mappingErrorResponse(exchange, ex.getMessage(), HttpStatus.BAD_REQUEST);

        }

         return mappingErrorResponse(exchange, DEFAULT_ERROR_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Mono<Void> mappingErrorResponse(ServerWebExchange exchange,
                                            String errorMessage,
                                            HttpStatus status) {

        log.debug("Error message: {}", errorMessage);
        DataBufferFactory dataBufferFactory = exchange.getResponse().bufferFactory();
        DataBuffer errorResponse;

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.TEXT_EVENT_STREAM);

        try {
            var error = ProblemDetail.forStatusAndDetail(status, errorMessage);
            error.setType(URI.create(""));

            errorResponse = dataBufferFactory.wrap(objectMapper.writeValueAsBytes(error));

            return exchange.getResponse().writeWith(Mono.just(errorResponse));

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
