package com.flab.woowahaneats.domain.cart.exception;

import org.springframework.http.HttpStatus;

public class RestaurantClosedException extends CartException {
    public RestaurantClosedException() {
        super(
                "현재 영업 중이 아닌 음식점에는 장바구니를 생성할 수 없습니다.",
                "RESTAURANT_CLOSED",
                HttpStatus.BAD_REQUEST
        );
    }
}