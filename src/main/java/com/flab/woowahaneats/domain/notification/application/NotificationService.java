package com.flab.woowahaneats.domain.notification.application;

import com.flab.woowahaneats.domain.auth.AuthContextHolder;
import com.flab.woowahaneats.domain.member.domain.Admin;
import com.flab.woowahaneats.domain.member.domain.Owner;
import com.flab.woowahaneats.domain.notification.controller.dto.NotificationResponse;
import com.flab.woowahaneats.domain.notification.infrastructure.SseEmitterManager;
import com.flab.woowahaneats.domain.restaurant.domain.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SseEmitterManager sseEmitterManager;

    public SseEmitter subscribe() {
        Admin admin = AuthContextHolder.getContext().getAdmin();
        return createSubscription("ADMIN_" + admin.getId());
    }

    public SseEmitter subscribeOwner() {
        Owner owner = AuthContextHolder.getContext().getOwner();
        return createSubscription("OWNER_" + owner.getId());
    }

    private SseEmitter createSubscription(String emitterId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        sseEmitterManager.save(emitterId, emitter);

        try {
            emitter.send(SseEmitter.event()
                    .name("connected")
                    .data("SSE 연결 성공"));
        } catch (Exception e) {
            sseEmitterManager.deleteById(emitterId);
        }

        return emitter;
    }

    public void sendToRole(String role, String message, Restaurant restaurant, Owner owner) {
        NotificationResponse response = NotificationResponse.of(message, restaurant, owner);
        sseEmitterManager.sendToRole(role, response);
    }

    public void sendToOwner(Long ownerId, String message, Restaurant restaurant) {
        String emitterId = "OWNER_" + ownerId;
        NotificationResponse response = NotificationResponse.of(message, restaurant, null);
        sseEmitterManager.sendToId(emitterId, response);
    }
}