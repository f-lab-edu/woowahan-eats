package com.flab.woowahaneats.domain.order.exception;

import org.springframework.http.HttpStatus;

public class MinOrderAmountNotMetException extends OrderException {
    public MinOrderAmountNotMetException(int minOrderAmt) {
        super(
                "최소 주문 금액은 " + minOrderAmt + "원 입니다.",
                "MIN_ORDER_AMOUNT_NOT_MET",
                HttpStatus.BAD_REQUEST
        );
    }
}