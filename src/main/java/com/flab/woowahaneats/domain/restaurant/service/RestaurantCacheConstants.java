package com.flab.woowahaneats.domain.restaurant.service;

import com.flab.woowahaneats.domain.restaurant.domain.RestaurantCategory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RestaurantCacheConstants {
    public static final String NEARBY_BY_CATEGORY = "nearbyRestaurantsByCategory";
    public static final int GEOHASH_PRECISION = 5;

    public static String buildKey(String geoHash, RestaurantCategory category) {
        return geoHash + ":" + category;
    }
}