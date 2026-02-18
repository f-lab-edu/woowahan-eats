package com.flab.woowahaneats.domain.restaurant.repository;

import com.flab.woowahaneats.domain.restaurant.domain.Restaurant;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class RestaurantRepositoryImpl implements RestaurantRepository {
    HashMap<Long, Restaurant> restaurantRepositoryImpl = new HashMap<>();

    public void save(Restaurant restaurant) {
        restaurantRepositoryImpl.put(restaurant.getId(), restaurant);
    }
}
