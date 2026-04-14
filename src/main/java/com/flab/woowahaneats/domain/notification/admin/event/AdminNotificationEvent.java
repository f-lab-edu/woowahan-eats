package com.flab.woowahaneats.domain.notification.admin.event;

import com.flab.woowahaneats.domain.notification.admin.controller.dto.AdminNotificationResponse;

public record AdminNotificationEvent(
        AdminNotificationResponse response
) {
}