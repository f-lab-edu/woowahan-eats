package com.flab.woowahaneats.domain.menu.application.exception;

import org.springframework.http.HttpStatus;

public class InvalidMenuException extends MenuException {
    public InvalidMenuException(String message) {
        super(
                message,
                "INVALID_MENU",
                HttpStatus.BAD_REQUEST
        );
    }
}