package com.flab.woowahaneats.domain.notification.controller.dto;

import java.time.LocalDateTime;

public record NotificationResponse(
        String message,
        Long relatedRestaurantId,
        boolean read,
        LocalDateTime createdAt
) {
    public static NotificationResponse of(String message, Long restaurantId) {
        return new NotificationResponse(
                message,
                restaurantId,
                false,
                LocalDateTime.now()
        );
    }
}