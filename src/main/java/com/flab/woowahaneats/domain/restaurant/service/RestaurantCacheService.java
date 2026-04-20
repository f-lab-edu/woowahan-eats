package com.flab.woowahaneats.domain.restaurant.service;

import ch.hsr.geohash.GeoHash;
import com.flab.woowahaneats.domain.restaurant.domain.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantCacheService {

    private final CacheManager cacheManager;

    private static final int GEOHASH_PRECISION = 5;

    public void evictNearbyRestaurantsByCategory(Restaurant restaurant) {
        Cache cache = cacheManager.getCache("nearbyRestaurantsByCategory");
        if (cache == null) {
            return;
        }
        String geoHash = GeoHash.withCharacterPrecision(
                restaurant.getLocation().latitude(),
                restaurant.getLocation().longitude(),
                GEOHASH_PRECISION
        ).toBase32();
        String key = geoHash + ":" + restaurant.getCategory();
        cache.evict(key);
    }
}