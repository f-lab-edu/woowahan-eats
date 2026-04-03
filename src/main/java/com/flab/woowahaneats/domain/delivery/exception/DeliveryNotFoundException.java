package com.flab.woowahaneats.domain.delivery.exception;

import org.springframework.http.HttpStatus;

public class DeliveryNotFoundException extends DeliveryException {
    public DeliveryNotFoundException() {
        super(
                "배달을 찾을 수 없습니다.",
                "DELIVERY_NOT_FOUND",
                HttpStatus.NOT_FOUND
        );
    }
}