package com.flab.woowahaneats.domain.rider.exception;

import com.flab.woowahaneats.global.exception.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.flab.woowahaneats.domain.rider.controller")
public class RiderExceptionHandler {

    @ExceptionHandler(RiderException.class)
    public ResponseEntity<ErrorResponse> handleRiderException(RiderException e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(ErrorResponse.of(e.getErrorCode(), e.getMessage()));
    }
}
