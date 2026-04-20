package com.flab.woowahaneats.domain.restaurant.owner.service;

import com.flab.woowahaneats.domain.auth.AuthContextHolder;
import com.flab.woowahaneats.domain.notification.admin.service.AdminNotificationService;
import com.flab.woowahaneats.domain.owner.domain.Owner;
import com.flab.woowahaneats.domain.restaurant.owner.controller.dto.RegisterRestaurantRequest;
import com.flab.woowahaneats.domain.restaurant.domain.Restaurant;
import com.flab.woowahaneats.domain.restaurant.domain.RestaurantOperationInfo;
import com.flab.woowahaneats.domain.restaurant.domain.RestaurantApprovalStatus;
import com.flab.woowahaneats.domain.restaurant.exception.RestaurantNotApprovedException;
import com.flab.woowahaneats.domain.restaurant.exception.RestaurantNotFoundException;
import com.flab.woowahaneats.domain.restaurant.exception.RestaurantNotOwnedException;
import com.flab.woowahaneats.domain.restaurant.exception.RestaurantOperationInfoNotFoundException;
import com.flab.woowahaneats.domain.restaurant.repository.RestaurantOperationInfoRepository;
import com.flab.woowahaneats.domain.restaurant.repository.RestaurantRepository;
import com.flab.woowahaneats.domain.restaurant.service.RestaurantCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OwnerRestaurantServiceImpl implements OwnerRestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantOperationInfoRepository restaurantOperationInfoRepository;
    private final AdminNotificationService adminNotificationService;
    private final RestaurantCacheService restaurantCacheService;

    @Override
    @Transactional
    public void registerRestaurant(RegisterRestaurantRequest restaurantRequest) {
        Owner owner = AuthContextHolder.getContext().getOwner();

        Restaurant restaurant = restaurantRepository.save(Restaurant.create(
                owner,
                restaurantRequest.name(),
                restaurantRequest.description(),
                restaurantRequest.address(),
                restaurantRequest.location(),
                restaurantRequest.category()
        ));

        RestaurantOperationInfo restaurantOperationInfo = RestaurantOperationInfo.create(
                restaurant.getId(),
                restaurantRequest.minOrderAmt(),
                restaurantRequest.deliveryFee()
        );

        restaurantOperationInfoRepository.save(restaurantOperationInfo);

        restaurantCacheService.evictNearbyRestaurantsByCategory(restaurant);

        adminNotificationService.notify(
                String.format("새로운 음식점 '%s'의 승인 요청이 있습니다.", restaurant.getName()),
                restaurant,
                owner
        );
    }

    @Override
    @Transactional
    public void openRestaurant(Long restaurantId) {
        Owner owner = AuthContextHolder.getContext().getOwner();

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(RestaurantNotFoundException::new);

        if (!restaurant.getOwner().getId().equals(owner.getId())) {
            throw new RestaurantNotOwnedException();
        }

        if (restaurant.getApprovalStatus() != RestaurantApprovalStatus.APPROVED) {
            throw new RestaurantNotApprovedException();
        }

        RestaurantOperationInfo restaurantOperationInfo = restaurantOperationInfoRepository.findById(restaurantId)
                .orElseThrow(RestaurantOperationInfoNotFoundException::new);

        RestaurantOperationInfo updateRestaurantOperationInfo = restaurantOperationInfo.toBuilder()
                .open(!restaurantOperationInfo.isOpen())
                .build();

        restaurantOperationInfoRepository.save(updateRestaurantOperationInfo);

        restaurantCacheService.evictNearbyRestaurantsByCategory(restaurant);
    }

}