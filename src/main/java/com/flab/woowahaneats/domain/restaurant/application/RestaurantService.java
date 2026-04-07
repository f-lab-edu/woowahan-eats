package com.flab.woowahaneats.domain.restaurant.application;

import com.flab.woowahaneats.domain.restaurant.controller.dto.RestaurantRequest;
import com.flab.woowahaneats.domain.restaurant.controller.dto.RestaurantResponse;

import java.util.List;

public interface RestaurantService {

    void registerRestaurant(RestaurantRequest restaurantRequest);

    RestaurantResponse getRestaurant(Long restaurantId);

    void openRestaurant(Long restaurantId);

    List<RestaurantResponse> getAllRestaurants();

    RestaurantResponse searchRestaurant(String name);
}
