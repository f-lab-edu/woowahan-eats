package com.flab.woowahaneats.domain.restaurant.application;

import com.flab.woowahaneats.domain.restaurant.controller.dto.RestaurantRequest;
import com.flab.woowahaneats.domain.restaurant.controller.dto.RestaurantResponse;

import java.util.List;

public interface RestaurantService {

    RestaurantResponse getRestaurant(Long restaurantId);

    List<RestaurantResponse> getAllRestaurants();

    RestaurantResponse searchRestaurant(String name);
}
