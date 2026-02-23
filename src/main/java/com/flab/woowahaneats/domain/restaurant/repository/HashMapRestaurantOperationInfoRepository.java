package com.flab.woowahaneats.domain.restaurant.repository;

import com.flab.woowahaneats.domain.restaurant.domain.RestaurantOperationInfo;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Optional;

@Repository
public class HashMapRestaurantOperationInfoRepository implements RestaurantOperationInfoRepository {

    HashMap<Long, RestaurantOperationInfo> restaurantOperationInfoRepository = new HashMap<>();

    public void save(RestaurantOperationInfo restaurantOperationInfo) {
        restaurantOperationInfoRepository.put(restaurantOperationInfo.getRestaurantId(), restaurantOperationInfo);
    }

    public Optional<RestaurantOperationInfo> findById(Long id) {
        return Optional.ofNullable(restaurantOperationInfoRepository.get(id));
    }

}
