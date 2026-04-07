package com.flab.woowahaneats.domain.notification.application;

import com.flab.woowahaneats.domain.owner.domain.Owner;
import com.flab.woowahaneats.domain.restaurant.domain.Restaurant;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {

    SseEmitter subscribe();

    SseEmitter subscribeOwner();

    void sendToRole(String role, String message, Restaurant restaurant, Owner owner);

    void sendToOwner(Long ownerId, String message, Restaurant restaurant);
}
