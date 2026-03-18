package com.flab.woowahaneats.domain.order.application.exception;

import org.springframework.http.HttpStatus;

public class RestaurantClosedException extends OrderException {
    public RestaurantClosedException() {
        super(
                "현재 영업 중이 아닙니다.",
                "RESTAURANT_CLOSED",
                HttpStatus.BAD_REQUEST
        );
    }
}