package com.flab.woowahaneats.domain.restaurant.application;

import com.flab.woowahaneats.domain.auth.OwnerAuthContext;
import com.flab.woowahaneats.domain.member.domain.Owner;
import com.flab.woowahaneats.domain.restaurant.application.exception.RestaurantNotFoundException;
import com.flab.woowahaneats.domain.restaurant.application.exception.RestaurantNotOwnedException;
import com.flab.woowahaneats.domain.restaurant.application.exception.RestaurantOperationInfoNotFoundException;
import com.flab.woowahaneats.domain.restaurant.controller.dto.RestaurantRequest;
import com.flab.woowahaneats.domain.restaurant.controller.dto.RestaurantResponse;
import com.flab.woowahaneats.domain.restaurant.domain.Restaurant;
import com.flab.woowahaneats.domain.restaurant.domain.RestaurantOperationInfo;
import com.flab.woowahaneats.domain.restaurant.repository.RestaurantOperationInfoRepository;
import com.flab.woowahaneats.domain.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantOperationInfoRepository restaurantOperationInfoRepository;

    public void registerRestaurant(RestaurantRequest restaurantRequest) {

        Owner owner = OwnerAuthContext.getOwner();

        Restaurant restaurant = Restaurant.builder()
                .id(restaurantRequest.id())
                .ownerId(owner.getId())
                .name(restaurantRequest.name())
                .description(restaurantRequest.description())
                .address(restaurantRequest.address())
                .location(restaurantRequest.location())
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

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(RestaurantNotFoundException::new);
        RestaurantOperationInfo restaurantOperationInfo = restaurantOperationInfoRepository.findById(restaurantId)
                .orElseThrow(RestaurantOperationInfoNotFoundException::new);

        return RestaurantResponse.of(restaurant, restaurantOperationInfo);
    }

    public void openRestaurant(Long restaurantId) {

        Owner owner = OwnerAuthContext.getOwner();

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(RestaurantNotFoundException::new);

        if (!restaurant.getOwnerId().equals(owner.getId())) {
            throw new RestaurantNotOwnedException();
        }

        RestaurantOperationInfo restaurantOperationInfo = restaurantOperationInfoRepository.findById(restaurantId)
                .orElseThrow(RestaurantOperationInfoNotFoundException::new);

        RestaurantOperationInfo updateRestaurantOperationInfo = restaurantOperationInfo.toBuilder()
                .open(!restaurantOperationInfo.isOpen())
                .build();

        restaurantOperationInfoRepository.save(updateRestaurantOperationInfo);
    }

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

    public RestaurantResponse searchRestaurant(String name) {
        Restaurant restaurant = restaurantRepository.findByName(name)
                .orElseThrow(RestaurantNotFoundException::new);

        RestaurantOperationInfo restaurantOperationInfo = restaurantOperationInfoRepository
                .findById(restaurant.getId())
                .orElseThrow(RestaurantOperationInfoNotFoundException::new);

        return RestaurantResponse.of(restaurant, restaurantOperationInfo);
    }
}