package com.flab.woowahaneats.domain.restaurant.repository;

import com.flab.woowahaneats.domain.restaurant.domain.RestaurantOperationInfo;

public interface RestaurantOperationInfoRepository {
    void save(RestaurantOperationInfo restaurantOperationInfo);
    RestaurantOperationInfo findById(Long id);
}
