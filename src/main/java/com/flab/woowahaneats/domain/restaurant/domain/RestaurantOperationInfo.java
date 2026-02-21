package com.flab.woowahaneats.domain.restaurant.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class RestaurantOperationInfo {
    private Long restaurantId;
    private int minOrderAmt;
    private int deliveryFee;
    private boolean open;
}
