package com.flab.woowahaneats.domain.payment.exception;

import org.springframework.http.HttpStatus;

public class PaymentProviderNotFoundException extends PaymentException {
    public PaymentProviderNotFoundException() {
        super(
                "지원하지 않는 결제사입니다.",
                "PAYMENT_PROVIDER_NOT_FOUND",
                HttpStatus.BAD_REQUEST
        );
    }
}
