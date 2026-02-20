package com.flab.woowahaneats.domain.restaurant.application;

import com.flab.woowahaneats.domain.restaurant.controller.dto.RestaurantRequest;
import com.flab.woowahaneats.domain.restaurant.controller.dto.RestaurantResponse;
import com.flab.woowahaneats.domain.restaurant.domain.Restaurant;
import com.flab.woowahaneats.domain.restaurant.domain.RestaurantOperationInfo;
import com.flab.woowahaneats.domain.restaurant.repository.RestaurantOperationInfoRepository;
import com.flab.woowahaneats.domain.restaurant.repository.RestaurantRepository;
import com.flab.woowahaneats.domain.restaurant.repository.RestaurantRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantOperationInfoRepository restaurantOperationInfoRepository;

    public void registerRestaurant(RestaurantRequest restaurantRequest) {

        Restaurant restaurant = Restaurant.builder()
                .id(restaurantRequest.id())
                .memberId(restaurantRequest.memberId())
                .name(restaurantRequest.name())
                .description(restaurantRequest.description())
                .address(restaurantRequest.address())
                .region(restaurantRequest.region())
                .avgRating(0.0)
                .build();

        RestaurantOperationInfo restaurantOperationInfo = RestaurantOperationInfo.builder()
                .restaurantId(restaurant.getId())
                .deliveryFee(restaurantRequest.deliveryFee())
                .minOrderAmt(restaurantRequest.minOrderAmt())
                .open(false)
                .build();

        restaurantRepository.save(restaurant);
        restaurantOperationInfoRepository.save(restaurantOperationInfo);
    }

    public RestaurantResponse getRestaurant(Long restaurantId) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow();
        RestaurantOperationInfo restaurantOperationInfo = restaurantOperationInfoRepository.findById(restaurantId).orElseThrow();

        return RestaurantResponse.of(restaurant, restaurantOperationInfo);
    }

    public void openRestaurant(Long restaurantId) {

        RestaurantOperationInfo restaurantOperationInfo = restaurantOperationInfoRepository.findById(restaurantId).orElseThrow();

        RestaurantOperationInfo updateRestaurantOperationInfo = restaurantOperationInfo.toBuilder()
                .open(!restaurantOperationInfo.isOpen())
                .build();

        restaurantOperationInfoRepository.save(updateRestaurantOperationInfo);
    }

    public List<RestaurantResponse> getAllRestaurants() {

        List<Restaurant> restaurants = restaurantRepository.findAll();
        List<RestaurantResponse> restaurantResponses = new ArrayList<>();

        for (Restaurant restaurant : restaurants) {

            RestaurantOperationInfo restaurantOperationInfo = restaurantOperationInfoRepository.findById(restaurant.getId()).orElseThrow();

            restaurantResponses.add(RestaurantResponse.of(restaurant, restaurantOperationInfo));

        }
        return restaurantResponses;
    }

    public RestaurantResponse searchRestaurant(String name) {
        Restaurant restaurant = restaurantRepository.findByName(name).orElseThrow();

        RestaurantOperationInfo restaurantOperationInfo = restaurantOperationInfoRepository
                .findById(restaurant.getId()).orElseThrow();

        return RestaurantResponse.of(restaurant, restaurantOperationInfo);
    }
}