package com.flab.woowahaneats.domain.restaurant.controller.dto;

import com.flab.woowahaneats.domain.common.vo.Address;
import com.flab.woowahaneats.domain.common.vo.Location;
import com.flab.woowahaneats.domain.restaurant.domain.Restaurant;
import com.flab.woowahaneats.domain.restaurant.domain.RestaurantOperationInfo;

public record RestaurantResponse (
        Long id,
        Long ownerId,
        String name,
        String description,
        Address address,
        Location location,
        double avgRating,
        int minOrderAmt,
        int deliveryFee,
        boolean open
){
    public static RestaurantResponse of(Restaurant restaurant, RestaurantOperationInfo restaurantOperationInfo) {
        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getOwnerId(),
                restaurant.getName(),
                restaurant.getDescription(),
                restaurant.getAddress(),
                restaurant.getLocation(),
                restaurant.getAvgRating(),
                restaurantOperationInfo.getMinOrderAmt(),
                restaurantOperationInfo.getDeliveryFee(),
                restaurantOperationInfo.isOpen()
        );

    }
}
