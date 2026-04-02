package com.flab.woowahaneats.domain.payment.exception;

import org.springframework.http.HttpStatus;

public class PaymentApprovalFailedException extends PaymentException {
    public PaymentApprovalFailedException(String message) {
        super(
                message,
                "PAYMENT_APPROVAL_FAILED",
                HttpStatus.BAD_REQUEST
        );
    }
}