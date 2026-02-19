package com.flab.woowahaneats.domain.restaurant.repository;

import com.flab.woowahaneats.domain.restaurant.domain.Restaurant;

import java.util.List;

public interface RestaurantRepository {
   void save(Restaurant restaurant);
   Restaurant findById(Long id);
   List<Restaurant> findAll();

}
