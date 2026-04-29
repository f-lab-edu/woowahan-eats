package com.flab.woowahaneats.domain.restaurant.user.service;

import static com.flab.woowahaneats.domain.restaurant.service.RestaurantCacheConstants.NEARBY_BY_CATEGORY;

import ch.hsr.geohash.GeoHash;
import com.flab.woowahaneats.domain.restaurant.domain.Restaurant;
import com.flab.woowahaneats.domain.restaurant.domain.RestaurantCategory;
import com.flab.woowahaneats.domain.restaurant.domain.RestaurantOperationInfo;
import com.flab.woowahaneats.domain.restaurant.exception.RestaurantOperationInfoNotFoundException;
import com.flab.woowahaneats.domain.restaurant.repository.RestaurantOperationInfoRepository;
import com.flab.woowahaneats.domain.restaurant.repository.RestaurantRepository;
import com.flab.woowahaneats.domain.restaurant.user.controller.dto.RestaurantResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NearbyRestaurantCacheService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantOperationInfoRepository restaurantOperationInfoRepository;

    private static final double NEARBY_RADIUS_KM = 30.0;

    @Cacheable(cacheNames = NEARBY_BY_CATEGORY,
               key = "#geoHash + ':' + #category",
               sync = true)
    @Transactional(readOnly = true)
    public List<RestaurantResponse> getNearbyRestaurantsByCategory(RestaurantCategory category, String geoHash) {
        GeoHash decoded = GeoHash.fromGeohashString(geoHash);
        double latitude = decoded.getBoundingBoxCenter().getLatitude();
        double longitude = decoded.getBoundingBoxCenter().getLongitude();

        List<Restaurant> restaurants = restaurantRepository.findAllByCategoryWithinRadius(
                category, latitude, longitude, NEARBY_RADIUS_KM);

        Map<Long, RestaurantOperationInfo> operationInfoMap = restaurantOperationInfoRepository
                .findAllByRestaurantIdIn(restaurants.stream().map(Restaurant::getId).toList())
                .stream()
                .collect(Collectors.toMap(RestaurantOperationInfo::getRestaurantId, Function.identity()));

        return new ArrayList<>(restaurants.stream()
                .map(restaurant -> {
                    RestaurantOperationInfo info = operationInfoMap.get(restaurant.getId());
                    if (info == null) {
                        throw new RestaurantOperationInfoNotFoundException();
                    }
                    return RestaurantResponse.of(restaurant, info);
                })
                .toList());
    }
}