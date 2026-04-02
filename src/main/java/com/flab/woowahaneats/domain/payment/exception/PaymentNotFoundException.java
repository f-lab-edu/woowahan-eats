package com.flab.woowahaneats.domain.payment.exception;

import org.springframework.http.HttpStatus;

public class PaymentNotFoundException extends PaymentException {
    public PaymentNotFoundException() {
        super(
                "결제 정보를 찾을 수 없습니다.",
                "PAYMENT_NOT_FOUND",
                HttpStatus.NOT_FOUND
        );
    }
}