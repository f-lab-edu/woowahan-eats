package com.flab.woowahaneats.domain.payment.exception;

import org.springframework.http.HttpStatus;

public class PaymentAmountMismatchException extends PaymentException {
    public PaymentAmountMismatchException() {
        super(
                "결제 금액이 일치하지 않습니다.",
                "PAYMENT_AMOUNT_MISMATCH",
                HttpStatus.BAD_REQUEST
        );
    }
}