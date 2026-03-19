package com.flab.woowahaneats.domain.order.application.exception;

import org.springframework.http.HttpStatus;

public class OrderNotFoundException extends OrderException {
    public OrderNotFoundException() {
        super(
                "주문을 찾을 수 없습니다.",
                "ORDER_NOT_FOUND",
                HttpStatus.NOT_FOUND
        );
    }
}