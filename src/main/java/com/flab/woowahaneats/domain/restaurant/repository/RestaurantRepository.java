package com.flab.woowahaneats.domain.restaurant.repository;

import com.flab.woowahaneats.domain.restaurant.domain.Restaurant;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository {
   void save(Restaurant restaurant);
   Optional<Restaurant> findById(Long id);
   List<Restaurant> findAll();
   Optional<Restaurant> findByName(String name);
}
