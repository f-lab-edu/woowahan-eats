package com.flab.woowahaneats.domain.restaurant.user.service;

import com.flab.woowahaneats.domain.auth.AuthContextHolder;
import com.flab.woowahaneats.domain.common.vo.Location;
import com.flab.woowahaneats.domain.restaurant.user.controller.dto.RestaurantResponse;
import com.flab.woowahaneats.domain.restaurant.domain.Restaurant;
import com.flab.woowahaneats.domain.restaurant.domain.RestaurantApprovalStatus;
import com.flab.woowahaneats.domain.restaurant.domain.RestaurantCategory;
import com.flab.woowahaneats.domain.restaurant.domain.RestaurantOperationInfo;
import com.flab.woowahaneats.domain.restaurant.exception.RestaurantNotFoundException;
import com.flab.woowahaneats.domain.restaurant.exception.RestaurantOperationInfoNotFoundException;
import com.flab.woowahaneats.domain.restaurant.repository.RestaurantOperationInfoRepository;
import com.flab.woowahaneats.domain.restaurant.repository.RestaurantRepository;
import ch.hsr.geohash.GeoHash;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserRestaurantServiceImpl implements UserRestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantOperationInfoRepository restaurantOperationInfoRepository;
    private final CaffeineCacheManager caffeineCacheManager;
    private final RedisCacheManager redisCacheManager;

    private static final String CACHE_NAME = "nearbyRestaurantsByCategory";
    private static final double NEARBY_RADIUS_KM = 30.0;

    @Override
    @Transactional(readOnly = true)
    public RestaurantResponse getRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findByIdAndApprovalStatus(restaurantId, RestaurantApprovalStatus.APPROVED)
                .orElseThrow(RestaurantNotFoundException::new);
        RestaurantOperationInfo restaurantOperationInfo = restaurantOperationInfoRepository.findById(restaurantId)
                .orElseThrow(RestaurantOperationInfoNotFoundException::new);

        return RestaurantResponse.of(restaurant, restaurantOperationInfo);
    }

private Map<Long, RestaurantOperationInfo> getOperationInfoMap(List<Restaurant> restaurants) {
        List<Long> restaurantIds = restaurants.stream()
                .map(Restaurant::getId)
                .toList();
        return restaurantOperationInfoRepository.findAllByRestaurantIdIn(restaurantIds)
                .stream()
                .collect(Collectors.toMap(RestaurantOperationInfo::getRestaurantId, info -> info));
    }

    private RestaurantOperationInfo getOperationInfo(Map<Long, RestaurantOperationInfo> map, Long restaurantId) {
        RestaurantOperationInfo info = map.get(restaurantId);
        if (info == null) {
            throw new RestaurantOperationInfoNotFoundException();
        }
        return info;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestaurantResponse> getRestaurantsByCategory(RestaurantCategory category) {
        List<Restaurant> restaurants = restaurantRepository.findAllByCategoryAndApprovalStatus(category, RestaurantApprovalStatus.APPROVED);
        Map<Long, RestaurantOperationInfo> operationInfoMap = getOperationInfoMap(restaurants);

        return restaurants.stream()
                .map(restaurant -> RestaurantResponse.of(restaurant, getOperationInfo(operationInfoMap, restaurant.getId())))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestaurantResponse> getNearbyRestaurants() {
        Location userLocation = AuthContextHolder.getContext().getUserLocation();
        double latitude = userLocation.latitude();
        double longitude = userLocation.longitude();

        List<Restaurant> restaurants = restaurantRepository.findAllWithinRadius(latitude, longitude, NEARBY_RADIUS_KM);
        Map<Long, RestaurantOperationInfo> operationInfoMap = getOperationInfoMap(restaurants);

        return restaurants.stream()
                .map(restaurant -> RestaurantResponse.of(restaurant, getOperationInfo(operationInfoMap, restaurant.getId())))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public List<RestaurantResponse> getNearbyRestaurantsByCategory(RestaurantCategory category, String geoHash) {
        String cacheKey = geoHash + ":" + category;

        Cache caffeineCache = Objects.requireNonNull(caffeineCacheManager.getCache(CACHE_NAME));
        Cache.ValueWrapper caffeineValue = caffeineCache.get(cacheKey);
        if (caffeineValue != null) {
            return (List<RestaurantResponse>) caffeineValue.get();
        }

        Cache redisCache = Objects.requireNonNull(redisCacheManager.getCache(CACHE_NAME));
        Cache.ValueWrapper redisValue = redisCache.get(cacheKey);
        if (redisValue != null) {
            List<RestaurantResponse> result = (List<RestaurantResponse>) redisValue.get();
            caffeineCache.put(cacheKey, result);
            return result;
        }

        List<RestaurantResponse> result = findNearbyRestaurantsByCategory(category, geoHash);
        redisCache.put(cacheKey, result);
        caffeineCache.put(cacheKey, result);
        return result;
    }

    private List<RestaurantResponse> findNearbyRestaurantsByCategory(RestaurantCategory category, String geoHash) {
        double latitude = GeoHash.fromGeohashString(geoHash).getBoundingBoxCenter().getLatitude();
        double longitude = GeoHash.fromGeohashString(geoHash).getBoundingBoxCenter().getLongitude();

        List<Restaurant> restaurants = restaurantRepository.findAllByCategoryWithinRadius(category, latitude, longitude, NEARBY_RADIUS_KM);
        Map<Long, RestaurantOperationInfo> operationInfoMap = getOperationInfoMap(restaurants);

        return restaurants.stream()
                .map(restaurant -> RestaurantResponse.of(restaurant, getOperationInfo(operationInfoMap, restaurant.getId())))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RestaurantResponse searchRestaurant(String name) {
        Restaurant restaurant = restaurantRepository.findFirstByNameContainingAndApprovalStatus(name, RestaurantApprovalStatus.APPROVED)
                .orElseThrow(RestaurantNotFoundException::new);

        RestaurantOperationInfo restaurantOperationInfo = restaurantOperationInfoRepository
                .findById(restaurant.getId())
                .orElseThrow(RestaurantOperationInfoNotFoundException::new);

        return RestaurantResponse.of(restaurant, restaurantOperationInfo);
    }
}