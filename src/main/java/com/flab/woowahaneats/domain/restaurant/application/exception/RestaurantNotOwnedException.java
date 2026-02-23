package com.flab.woowahaneats.domain.restaurant.application.exception;

import org.springframework.http.HttpStatus;

public class RestaurantNotOwnedException extends RestaurantException {
  public RestaurantNotOwnedException() {
    super(
            "본인 소유의 음식점만 영업 상태를 변경할 수 있습니다.",
            "RESTAURANT_NOT_OWNED",
            HttpStatus.FORBIDDEN
    );
  }

}
