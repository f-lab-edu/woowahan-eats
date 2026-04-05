package com.flab.woowahaneats.domain.user.exception;

import com.flab.woowahaneats.global.exception.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.flab.woowahaneats.domain.user.controller")
public class UserExceptionHandler {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResponse> handleUserException(UserException e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(ErrorResponse.of(e.getErrorCode(), e.getMessage()));
    }
}
