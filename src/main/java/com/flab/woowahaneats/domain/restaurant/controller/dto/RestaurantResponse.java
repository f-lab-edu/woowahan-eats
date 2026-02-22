package com.flab.woowahaneats.domain.restaurant.controller.dto;

import com.flab.woowahaneats.domain.common.vo.Address;
import com.flab.woowahaneats.domain.restaurant.domain.Restaurant;
import com.flab.woowahaneats.domain.restaurant.domain.RestaurantOperationInfo;

public record RestaurantResponse (
        Long id,
        Long memberId,
        String name,
        String description,
        Address address,
        String region,
        double avgRating,
        int minOrderAmt,
        int deliveryFee,
        boolean open
){
    public static RestaurantResponse of(Restaurant restaurant, RestaurantOperationInfo restaurantOperationInfo) {
        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getMemberId(),
                restaurant.getName(),
                restaurant.getDescription(),
                restaurant.getAddress(),
                restaurant.getRegion(),
                restaurant.getAvgRating(),
                restaurantOperationInfo.getMinOrderAmt(),
                restaurantOperationInfo.getDeliveryFee(),
                restaurantOperationInfo.isOpen()
        );

    }
}
