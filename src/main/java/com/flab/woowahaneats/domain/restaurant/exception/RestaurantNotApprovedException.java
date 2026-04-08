package com.flab.woowahaneats.domain.restaurant.exception;

import org.springframework.http.HttpStatus;

public class RestaurantNotApprovedException extends RestaurantException {
  public RestaurantNotApprovedException() {
    super(
            "승인되지 않은 음식점은 영업을 시작할 수 없습니다.",
            "RESTAURANT_NOT_APPROVED",
            HttpStatus.FORBIDDEN
    );
  }

}