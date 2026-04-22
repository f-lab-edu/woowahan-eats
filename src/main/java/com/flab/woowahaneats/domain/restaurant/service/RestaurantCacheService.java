package com.flab.woowahaneats.domain.restaurant.service;

import com.flab.woowahaneats.domain.restaurant.domain.Restaurant;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
public class RestaurantCacheService {

    @CacheEvict(cacheNames = "nearbyRestaurantsByCategory",
                keyGenerator = "restaurantCacheKeyGenerator")
    public void evictNearbyRestaurantsByCategory(Restaurant restaurant) {
    }
}