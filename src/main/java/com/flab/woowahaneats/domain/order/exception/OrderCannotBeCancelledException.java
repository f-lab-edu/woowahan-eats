package com.flab.woowahaneats.domain.order.exception;

import org.springframework.http.HttpStatus;

public class OrderCannotBeCancelledException extends OrderException {
    public OrderCannotBeCancelledException(String message) {
        super(
                message,
                "ORDER_CANNOT_BE_CANCELLED",
                HttpStatus.BAD_REQUEST
        );
    }
}