package com.flab.woowahaneats.domain.payment.exception;

import org.springframework.http.HttpStatus;

public class PaymentNotBelongToUserException extends PaymentException {
    public PaymentNotBelongToUserException() {
        super(
                "해당 사용자의 결제가 아닙니다.",
                "PAYMENT_NOT_BELONG_TO_USER",
                HttpStatus.FORBIDDEN
        );
    }
}
