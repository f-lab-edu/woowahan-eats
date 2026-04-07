package com.flab.woowahaneats.domain.notification.application;

import com.flab.woowahaneats.domain.admin.domain.Admin;
import com.flab.woowahaneats.domain.auth.AuthContextHolder;
import com.flab.woowahaneats.domain.notification.controller.dto.NotificationResponse;
import com.flab.woowahaneats.domain.notification.infrastructure.SseEmitterManager;
import com.flab.woowahaneats.domain.owner.domain.Owner;
import com.flab.woowahaneats.domain.restaurant.domain.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final SseEmitterManager sseEmitterManager;

    @Override
    public SseEmitter subscribe() {
        Admin admin = AuthContextHolder.getContext().getAdmin();
        return createSubscription("ADMIN_" + admin.getId());
    }

    @Override
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

    @Override
    public void sendToRole(String role, String message, Restaurant restaurant, Owner owner) {
        NotificationResponse response = NotificationResponse.of(message, restaurant, owner);
        sseEmitterManager.sendToRole(role, response);
    }

    @Override
    public void sendToOwner(Long ownerId, String message, Restaurant restaurant) {
        String emitterId = "OWNER_" + ownerId;
        NotificationResponse response = NotificationResponse.of(message, restaurant, null);
        sseEmitterManager.sendToId(emitterId, response);
    }
}
