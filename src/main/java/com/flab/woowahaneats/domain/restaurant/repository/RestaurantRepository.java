package com.flab.woowahaneats.domain.restaurant.repository;

import com.flab.woowahaneats.domain.restaurant.domain.Restaurant;

public interface RestaurantRepository {
   void save(Restaurant restaurant);
   Restaurant findById(Long id);
}
