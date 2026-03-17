package com.flab.woowahaneats.domain.cart.application.exception;

import org.springframework.http.HttpStatus;

public class RestaurantMismatchException extends CartException {
    public RestaurantMismatchException() {
        super(
                "다른 음식점의 메뉴는 추가할 수 없습니다.",
                "RESTAURANT_MISMATCH",
                HttpStatus.BAD_REQUEST
        );
    }
}