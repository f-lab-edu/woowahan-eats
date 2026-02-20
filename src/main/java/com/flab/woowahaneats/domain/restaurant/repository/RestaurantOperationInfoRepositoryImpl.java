package com.flab.woowahaneats.domain.restaurant.repository;

import com.flab.woowahaneats.domain.restaurant.domain.RestaurantOperationInfo;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class RestaurantOperationInfoRepositoryImpl implements RestaurantOperationInfoRepository {

    HashMap<Long, RestaurantOperationInfo> restaurantOperationInfoRepository = new HashMap<>();

    public void save(RestaurantOperationInfo restaurantOperationInfo) {
        restaurantOperationInfoRepository.put(restaurantOperationInfo.getRestaurantId(), restaurantOperationInfo);
    }

    public RestaurantOperationInfo findById(Long id) {
        return restaurantOperationInfoRepository.get(id);
    }

}
