package com.flab.woowahaneats.domain.notification.owner.service;

import com.flab.woowahaneats.domain.notification.owner.controller.dto.OwnerNotificationResponse;
import com.flab.woowahaneats.domain.owner.domain.Owner;
import com.flab.woowahaneats.domain.restaurant.domain.Restaurant;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

public interface OwnerNotificationService {

    void notify(Owner owner, String message, Restaurant restaurant);

    List<OwnerNotificationResponse> getUnreadNotifications();

    SseEmitter subscribe();
}
