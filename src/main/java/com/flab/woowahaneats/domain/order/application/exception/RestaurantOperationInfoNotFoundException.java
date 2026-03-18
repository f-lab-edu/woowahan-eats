package com.flab.woowahaneats.domain.order.application.exception;

import org.springframework.http.HttpStatus;

public class RestaurantOperationInfoNotFoundException extends OrderException {
    public RestaurantOperationInfoNotFoundException() {
        super(
                "식당 운영 정보를 찾을 수 없습니다.",
                "RESTAURANT_OPERATION_INFO_NOT_FOUND",
                HttpStatus.NOT_FOUND
        );
    }
}