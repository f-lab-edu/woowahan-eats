package com.flab.woowahaneats.domain.restaurant.application;

import com.flab.woowahaneats.domain.restaurant.controller.dto.RestaurantRequest;
import com.flab.woowahaneats.domain.restaurant.domain.Restaurant;
import com.flab.woowahaneats.domain.restaurant.repository.RestaurantRepository;
import com.flab.woowahaneats.domain.restaurant.repository.RestaurantRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
                .avgRating(0.0)
                .build();

        restaurantRepository.save(restaurant);
    }

    public Restaurant getRestaurant(Long restaurantId) {
        return restaurantRepository.findById(restaurantId);
    }

    public void restaurantOpen(Long restaurantId) {
        Restaurant restaurant = getRestaurant(restaurantId).toBuilder()
                .open(!getRestaurant(restaurantId).isOpen())
                .build();

        restaurantRepository.save(restaurant);
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }
}