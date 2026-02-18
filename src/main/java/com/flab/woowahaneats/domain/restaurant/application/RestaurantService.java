package com.flab.woowahaneats.domain.restaurant.application;

import com.flab.woowahaneats.domain.restaurant.controller.dto.RestaurantRequest;
import com.flab.woowahaneats.domain.restaurant.domain.Restaurant;
import com.flab.woowahaneats.domain.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public void restaurantRegister(RestaurantRequest restaurantRequest) {

        Restaurant restaurant = Restaurant.builder()
                .id(restaurantRequest.id())
                .memberId(restaurantRequest.memberId())
                .name(restaurantRequest.name())
                .description(restaurantRequest.description())
                .address(restaurantRequest.address())
                .minOrderAmt(restaurantRequest.minOrderAmt())
                .deliveryFee(restaurantRequest.deliveryFee())
                .region(restaurantRequest.region())
                .open(false)
                .avgRating(BigDecimal.ZERO)
                .build();

        restaurantRepository.save(restaurant);
    }

}
