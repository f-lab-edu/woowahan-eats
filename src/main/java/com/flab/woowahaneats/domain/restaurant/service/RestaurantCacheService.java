package com.flab.woowahaneats.domain.restaurant.service;

import ch.hsr.geohash.GeoHash;
import com.flab.woowahaneats.domain.restaurant.domain.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantCacheService {

    private final RedisCacheManager redisCacheManager;
    private final CaffeineCacheManager caffeineCacheManager;

    private static final String CACHE_NAME = "nearbyRestaurantsByCategory";
    private static final int GEOHASH_PRECISION = 5;

    public void evictNearbyRestaurantsByCategory(Restaurant restaurant) {
        String geoHash = GeoHash.withCharacterPrecision(
                restaurant.getLocation().latitude(),
                restaurant.getLocation().longitude(),
                GEOHASH_PRECISION
        ).toBase32();
        String key = geoHash + ":" + restaurant.getCategory();

        Cache redisCache = redisCacheManager.getCache(CACHE_NAME);
        if (redisCache != null) {
            redisCache.evict(key);
        }

        Cache caffeineCache = caffeineCacheManager.getCache(CACHE_NAME);
        if (caffeineCache != null) {
            caffeineCache.evict(key);
        }
    }
}