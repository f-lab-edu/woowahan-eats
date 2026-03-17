package com.flab.woowahaneats.domain.cart.application.exception;

import org.springframework.http.HttpStatus;

public class InvalidCartException extends CartException {
    public InvalidCartException(String message) {
        super(
                message,
                "INVALID_CART",
                HttpStatus.BAD_REQUEST
        );
    }
}