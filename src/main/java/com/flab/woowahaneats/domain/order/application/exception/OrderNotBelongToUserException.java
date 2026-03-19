package com.flab.woowahaneats.domain.order.application.exception;

import org.springframework.http.HttpStatus;

public class OrderNotBelongToUserException extends OrderException {
    public OrderNotBelongToUserException() {
        super(
                "해당 주문에 접근할 권한이 없습니다.",
                "ORDER_NOT_BELONG_TO_USER",
                HttpStatus.FORBIDDEN
        );
    }
}