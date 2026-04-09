package com.flab.woowahaneats.domain.notification.owner.controller.dto;

import com.flab.woowahaneats.domain.notification.owner.domain.OwnerNotification;

import java.time.LocalDateTime;

public record OwnerNotificationResponse(
        Long id,
        String message,
        LocalDateTime createdAt
) {
    public static OwnerNotificationResponse from(OwnerNotification notification) {
        return new OwnerNotificationResponse(
                notification.getId(),
                notification.getMessage(),
                notification.getCreatedAt()
        );
    }
}
