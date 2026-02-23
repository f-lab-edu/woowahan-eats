package com.flab.woowahaneats.domain.restaurant.application.exception;

import org.springframework.http.HttpStatus;

public class RestaurantNotFoundException extends RestaurantException {
    public RestaurantNotFoundException() {
        super(
                "음식점을 찾을 수 없습니다. ",
                "RESTAURANT_NOT_FOUND",
                HttpStatus.NOT_FOUND
        );
    }
}
