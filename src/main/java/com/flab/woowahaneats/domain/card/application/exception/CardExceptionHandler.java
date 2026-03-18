package com.flab.woowahaneats.domain.card.application.exception;

import com.flab.woowahaneats.global.exception.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.flab.woowahaneats.domain.card")
public class CardExceptionHandler {

    @ExceptionHandler(CardException.class)
    public ResponseEntity<ErrorResponse> handleCardException(CardException e) {

        return ResponseEntity
                .status(e.getHttpStatus())
                .body(ErrorResponse.of(e.getErrorCode(), e.getMessage()));
    }

}