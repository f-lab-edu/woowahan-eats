package com.flab.woowahaneats.domain.restaurant.repository;

import com.flab.woowahaneats.domain.restaurant.domain.Restaurant;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class RestaurantRepositoryImpl implements RestaurantRepository {
    HashMap<Long, Restaurant> restaurantRepositoryImpl = new HashMap<>();

    public void save(Restaurant restaurant) {
        restaurantRepositoryImpl.put(restaurant.getId(), restaurant);
    }

    public Restaurant findById(Long id) {
        return restaurantRepositoryImpl.get(id);
    }

    public List<Restaurant> findAll() {
        return new ArrayList<>(restaurantRepositoryImpl.values());
    }
}
