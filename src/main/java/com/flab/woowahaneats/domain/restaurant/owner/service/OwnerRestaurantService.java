package com.flab.woowahaneats.domain.restaurant.owner.service;

import com.flab.woowahaneats.domain.restaurant.controller.dto.RestaurantRequest;

public interface OwnerRestaurantService {

    void registerRestaurant(RestaurantRequest restaurantRequest);

    void openRestaurant(Long restaurantId);
}