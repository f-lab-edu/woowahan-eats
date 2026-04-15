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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserRestaurantServiceImpl implements UserRestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantOperationInfoRepository restaurantOperationInfoRepository;

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

    private static final double NEARBY_RADIUS_KM = 30.0;

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
    public List<RestaurantResponse> getNearbyRestaurantsByCategory(RestaurantCategory category) {
        Location userLocation = AuthContextHolder.getContext().getUserLocation();
        double latitude = userLocation.latitude();
        double longitude = userLocation.longitude();

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