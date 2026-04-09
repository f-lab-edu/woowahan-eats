package com.flab.woowahaneats.domain.restaurant.exception;

import org.springframework.http.HttpStatus;

public class RestaurantAlreadyApprovedException extends RestaurantException {
    public RestaurantAlreadyApprovedException() {
        super(
                "이미 승인된 음식점입니다.",
                "RESTAURANT_ALREADY_APPROVED",
                HttpStatus.CONFLICT
        );
    }
}
