package com.flab.woowahaneats.domain.restaurant.owner.service;

import com.flab.woowahaneats.domain.restaurant.owner.controller.dto.RegisterRestaurantRequest;

public interface OwnerRestaurantService {

    void registerRestaurant(RegisterRestaurantRequest restaurantRequest);

    void openRestaurant(Long restaurantId);
}