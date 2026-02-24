package com.flab.woowahaneats.domain.restaurant.application.exception;

import org.springframework.http.HttpStatus;

public class RestaurantOperationInfoNotFoundException extends RestaurantException {
    public RestaurantOperationInfoNotFoundException() {
        super(
                "음식점 운영 정보를 찾을 수 없습니다.",
                "RESTAURANT_OPERATION_INFO_NOT_FOUND",
                HttpStatus.NOT_FOUND
        );
    }
}