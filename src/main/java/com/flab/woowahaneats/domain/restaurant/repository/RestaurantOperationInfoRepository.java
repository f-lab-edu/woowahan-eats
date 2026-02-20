package com.flab.woowahaneats.domain.restaurant.repository;

import com.flab.woowahaneats.domain.restaurant.domain.RestaurantOperationInfo;

import java.util.Optional;

public interface RestaurantOperationInfoRepository {
    void save(RestaurantOperationInfo restaurantOperationInfo);
    Optional<RestaurantOperationInfo> findById(Long id);
}
