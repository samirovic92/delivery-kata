package com.carrefour.deliverykata.api;

import com.carrefour.deliverykata.api.Dto.ErrorMessage;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

@org.springframework.web.bind.annotation.RestControllerAdvice
public class RestControllerAdvice {

    @ExceptionHandler(Exception.class)
    public Mono<ErrorMessage> handle(Exception exception) {
        return Mono.just(new ErrorMessage(exception.getMessage()));
    }
}
