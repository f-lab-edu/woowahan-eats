package com.flab.woowahaneats.domain.user.exception;

import org.springframework.http.HttpStatus;

public class InvalidUserException extends UserException {
    public InvalidUserException(String message) {
        super(
                message,
                "INVALID_USER",
                HttpStatus.BAD_REQUEST
        );
    }
}
