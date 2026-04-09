package com.flab.woowahaneats.domain.notification.admin.service;

import com.flab.woowahaneats.domain.notification.admin.controller.dto.AdminNotificationResponse;
import com.flab.woowahaneats.domain.owner.domain.Owner;
import com.flab.woowahaneats.domain.restaurant.domain.Restaurant;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

public interface AdminNotificationService {

    void notify(String message, Restaurant restaurant, Owner owner);

    List<AdminNotificationResponse> getUnreadNotifications();

    SseEmitter subscribe();
}
