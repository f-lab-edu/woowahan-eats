package com.flab.woowahaneats.domain.card.application.exception;

import org.springframework.http.HttpStatus;

public class InvalidCardException extends CardException {
    public InvalidCardException(String message) {
        super(
                message,
                "INVALID_CARD",
                HttpStatus.BAD_REQUEST
        );
    }
}