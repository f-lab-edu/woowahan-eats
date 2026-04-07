package com.flab.woowahaneats.domain.restaurant.admin.service;

public interface AdminRestaurantService {

    void approveRestaurant(Long restaurantId);

    void rejectRestaurant(Long restaurantId);
}
