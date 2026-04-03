package com.flab.woowahaneats.domain.payment.exception;

import org.springframework.http.HttpStatus;

public class PaymentOrderNotFoundException extends PaymentException {
    public PaymentOrderNotFoundException() {
        super(
                "결제에 연결된 주문 정보를 찾을 수 없습니다.",
                "PAYMENT_ORDER_NOT_FOUND",
                HttpStatus.NOT_FOUND
        );
    }
}
