package com.flab.woowahaneats.domain.restaurant.user.service;

import com.flab.woowahaneats.domain.restaurant.user.controller.dto.RestaurantResponse;

import java.util.List;

public interface UserRestaurantService {

    RestaurantResponse getRestaurant(Long restaurantId);

    List<RestaurantResponse> getAllRestaurants();

    RestaurantResponse searchRestaurant(String name);
}