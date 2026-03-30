package com.flab.woowahaneats.domain.payment.exception;

import org.springframework.http.HttpStatus;

public class InvalidPaymentStatusException extends PaymentException {
    public InvalidPaymentStatusException(String message) {
        super(
                message,
                "PAYMENT_INVALID_STATUS",
                HttpStatus.BAD_REQUEST
        );
    }
}