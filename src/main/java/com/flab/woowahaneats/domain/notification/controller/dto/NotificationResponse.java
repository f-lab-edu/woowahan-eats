package com.flab.woowahaneats.domain.notification.controller.dto;

import com.flab.woowahaneats.domain.common.vo.Address;
import com.flab.woowahaneats.domain.common.vo.Location;
import com.flab.woowahaneats.domain.owner.domain.Owner;
import com.flab.woowahaneats.domain.restaurant.domain.Restaurant;

import java.time.LocalDateTime;

public record NotificationResponse(
        String message,
        RestaurantInfo restaurant,
        OwnerInfo owner,
        boolean read,
        LocalDateTime createdAt
) {
    public record RestaurantInfo(
            Long id,
            String name,
            Address address,
            Location location
    ) {}

    public record OwnerInfo(
            String name,
            String phoneNumber,
            String businessRegistrationCertUrl,
            String businessNotificationCertUrl
    ) {}

    public static NotificationResponse of(String message, Restaurant restaurant, Owner owner) {
        return new NotificationResponse(
                message,
                restaurant != null ? new RestaurantInfo(
                        restaurant.getId(),
                        restaurant.getName(),
                        restaurant.getAddress(),
                        restaurant.getLocation()
                ) : null,
                owner != null ? new OwnerInfo(
                        owner.getName(),
                        owner.getPhoneNumber(),
                        owner.getBusinessRegistrationCertUrl(),
                        owner.getBusinessNotificationCertUrl()
                ) : null,
                false,
                LocalDateTime.now()
        );
    }
}