package com.flab.woowahaneats.domain.owner.exception;

import com.flab.woowahaneats.global.exception.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.flab.woowahaneats.domain.owner.controller")
public class OwnerExceptionHandler {

    @ExceptionHandler(OwnerException.class)
    public ResponseEntity<ErrorResponse> handleOwnerException(OwnerException e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(ErrorResponse.of(e.getErrorCode(), e.getMessage()));
    }
}
