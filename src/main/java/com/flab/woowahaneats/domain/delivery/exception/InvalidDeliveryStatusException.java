package com.flab.woowahaneats.domain.delivery.exception;

import org.springframework.http.HttpStatus;

public class InvalidDeliveryStatusException extends DeliveryException {
    public InvalidDeliveryStatusException(String message) {
        super(
                message,
                "INVALID_DELIVERY_STATUS",
                HttpStatus.BAD_REQUEST
        );
    }
}