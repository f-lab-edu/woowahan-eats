package com.flab.woowahaneats.domain.notification.admin.controller.dto;

import com.flab.woowahaneats.domain.common.vo.Address;
import com.flab.woowahaneats.domain.common.vo.Location;
import com.flab.woowahaneats.domain.notification.admin.domain.AdminNotification;

import java.time.LocalDateTime;

public record AdminNotificationResponse(
        Long id,
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

    public static AdminNotificationResponse from(AdminNotification notification) {
        return new AdminNotificationResponse(
                notification.getId(),
                notification.getMessage(),
                notification.getRestaurant() != null ? new RestaurantInfo(
                        notification.getRestaurant().getId(),
                        notification.getRestaurant().getName(),
                        notification.getRestaurant().getAddress(),
                        notification.getRestaurant().getLocation()
                ) : null,
                notification.getOwner() != null ? new OwnerInfo(
                        notification.getOwner().getName(),
                        notification.getOwner().getPhoneNumber(),
                        notification.getOwner().getBusinessRegistrationCertUrl(),
                        notification.getOwner().getBusinessNotificationCertUrl()
                ) : null,
                notification.isRead(),
                notification.getCreatedAt()
        );
    }
}
