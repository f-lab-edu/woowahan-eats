package com.flab.woowahaneats.domain.delivery.exception;

import org.springframework.http.HttpStatus;

public class OrderNotReadyForDeliveryException extends DeliveryException {
    public OrderNotReadyForDeliveryException() {
        super(
                "조리가 완료된 주문만 배달을 시작할 수 있습니다.",
                "ORDER_NOT_READY_FOR_DELIVERY",
                HttpStatus.BAD_REQUEST
        );
    }
}