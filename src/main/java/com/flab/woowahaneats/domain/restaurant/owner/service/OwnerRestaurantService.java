package com.flab.woowahaneats.domain.restaurant.owner.service;

import com.flab.woowahaneats.domain.restaurant.owner.controller.dto.RegisterRestaurantRequest;

public interface OwnerRestaurantService {

    void registerRestaurant(RegisterRestaurantRequest restaurantRequest);

    void openRestaurant(Long restaurantId);

    /**
     * 현재 인증된 점주가 소유한 음식점 ID를 반환한다.
     * 점주가 여러 음식점을 운영하는 경우 첫 번째 음식점을 반환한다.
     */
    Long getMyRestaurantId();
}