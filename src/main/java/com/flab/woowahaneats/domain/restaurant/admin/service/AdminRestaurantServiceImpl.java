package com.flab.woowahaneats.domain.restaurant.admin.service;

import com.flab.woowahaneats.domain.auth.AuthContextHolder;
import com.flab.woowahaneats.domain.notification.application.NotificationService;
import com.flab.woowahaneats.domain.restaurant.domain.Restaurant;
import com.flab.woowahaneats.domain.restaurant.domain.RestaurantApprovalStatus;
import com.flab.woowahaneats.domain.restaurant.exception.RestaurantNotFoundException;
import com.flab.woowahaneats.domain.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminRestaurantServiceImpl implements AdminRestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public void approveRestaurant(Long restaurantId) {
        updateApprovalStatus(restaurantId, RestaurantApprovalStatus.APPROVED);
    }

    @Override
    @Transactional
    public void rejectRestaurant(Long restaurantId) {
        updateApprovalStatus(restaurantId, RestaurantApprovalStatus.REJECTED);
    }

    private void updateApprovalStatus(Long restaurantId, RestaurantApprovalStatus status) {
        AuthContextHolder.getContext().getAdmin();

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(RestaurantNotFoundException::new);

        Restaurant updatedRestaurant = restaurant.toBuilder()
                .approvalStatus(status)
                .build();

        restaurantRepository.save(updatedRestaurant);

        String message = status == RestaurantApprovalStatus.APPROVED
                ? String.format("음식점 '%s'의 등록이 승인되었습니다.", restaurant.getName())
                : String.format("음식점 '%s'의 등록이 거절되었습니다.", restaurant.getName());

        notificationService.sendToOwner(restaurant.getOwner().getId(), message, restaurant);
    }
}
