package com.flab.woowahaneats.domain.owner.exception;

import org.springframework.http.HttpStatus;

public class InvalidOwnerException extends OwnerException {
    public InvalidOwnerException(String message) {
        super(
                message,
                "INVALID_OWNER",
                HttpStatus.BAD_REQUEST
        );
    }
}
