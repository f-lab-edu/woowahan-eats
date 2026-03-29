package com.flab.woowahaneats.domain.order.exception;

import org.springframework.http.HttpStatus;

public class InvalidOrderStatusException extends OrderException {
    public InvalidOrderStatusException(String message) {
        super(
                message,
                "ORDER_INVALID_STATUS",
                HttpStatus.BAD_REQUEST
        );
    }
}