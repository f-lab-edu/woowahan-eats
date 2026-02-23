package com.flab.woowahaneats.domain.restaurant.repository;

import com.flab.woowahaneats.domain.restaurant.domain.Restaurant;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class HashMapRestaurantRepository implements RestaurantRepository {
    HashMap<Long, Restaurant> HashMapRestaurantRepository = new HashMap<>();

    public void save(Restaurant restaurant) {
        HashMapRestaurantRepository.put(restaurant.getId(), restaurant);
    }

    public Optional<Restaurant> findById(Long id) {
        return Optional.ofNullable(HashMapRestaurantRepository.get(id));
    }

    public List<Restaurant> findAll() {
        return new ArrayList<>(HashMapRestaurantRepository.values());
    }

    public Optional<Restaurant> findByName(String name) {
        return HashMapRestaurantRepository.values().stream()
                .filter(restaurant -> restaurant.getName().contains(name))
                .findFirst();
    }
}
