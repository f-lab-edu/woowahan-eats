package com.flab.woowahaneats.domain.restaurant.service;

import static com.flab.woowahaneats.domain.restaurant.service.RestaurantCacheConstants.NEARBY_BY_CATEGORY;

import com.flab.woowahaneats.domain.restaurant.domain.Restaurant;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
public class RestaurantCacheService {

    @CacheEvict(cacheNames = NEARBY_BY_CATEGORY,
                keyGenerator = "restaurantCacheKeyGenerator")
    public void evictNearbyRestaurantsByCategory(Restaurant restaurant) {
    }
}