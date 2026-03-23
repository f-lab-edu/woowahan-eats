package com.flab.woowahaneats.domain.order.exception;

import org.springframework.http.HttpStatus;

public class OrderCannotBeApprovedException extends OrderException {
    public OrderCannotBeApprovedException(String message) {
        super(
                message,
                "주문을 승인할 수 없습니다.",
                HttpStatus.BAD_REQUEST
        );
    }
}