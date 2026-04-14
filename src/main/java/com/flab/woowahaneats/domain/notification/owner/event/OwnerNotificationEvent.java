package com.flab.woowahaneats.domain.notification.owner.event;

import com.flab.woowahaneats.domain.notification.owner.controller.dto.OwnerNotificationResponse;

public record OwnerNotificationEvent(
        Long ownerId,
        OwnerNotificationResponse response
) {
}