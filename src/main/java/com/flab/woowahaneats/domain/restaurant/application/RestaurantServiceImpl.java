package com.flab.woowahaneats.domain.restaurant.application;

import com.flab.woowahaneats.domain.restaurant.controller.dto.RestaurantResponse;
import com.flab.woowahaneats.domain.restaurant.domain.Restaurant;
import com.flab.woowahaneats.domain.restaurant.domain.RestaurantOperationInfo;
import com.flab.woowahaneats.domain.restaurant.exception.RestaurantNotFoundException;
import com.flab.woowahaneats.domain.restaurant.exception.RestaurantOperationInfoNotFoundException;
import com.flab.woowahaneats.domain.restaurant.repository.RestaurantOperationInfoRepository;
import com.flab.woowahaneats.domain.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantOperationInfoRepository restaurantOperationInfoRepository;

    @Override
    @Transactional(readOnly = true)
    public RestaurantResponse getRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(RestaurantNotFoundException::new);
        RestaurantOperationInfo restaurantOperationInfo = restaurantOperationInfoRepository.findById(restaurantId)
                .orElseThrow(RestaurantOperationInfoNotFoundException::new);

        return RestaurantResponse.of(restaurant, restaurantOperationInfo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestaurantResponse> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        List<RestaurantResponse> restaurantResponses = new ArrayList<>();

        for (Restaurant restaurant : restaurants) {
            RestaurantOperationInfo restaurantOperationInfo = restaurantOperationInfoRepository.findById(restaurant.getId())
                    .orElseThrow(RestaurantOperationInfoNotFoundException::new);

            restaurantResponses.add(RestaurantResponse.of(restaurant, restaurantOperationInfo));
        }
        return restaurantResponses;
    }

    @Override
    @Transactional(readOnly = true)
    public RestaurantResponse searchRestaurant(String name) {
        Restaurant restaurant = restaurantRepository.findFirstByNameContaining(name)
                .orElseThrow(RestaurantNotFoundException::new);

        RestaurantOperationInfo restaurantOperationInfo = restaurantOperationInfoRepository
                .findById(restaurant.getId())
                .orElseThrow(RestaurantOperationInfoNotFoundException::new);

        return RestaurantResponse.of(restaurant, restaurantOperationInfo);
    }
}
