package com.flab.woowahaneats.domain.notification.application;

import com.flab.woowahaneats.domain.auth.AuthContextHolder;
import com.flab.woowahaneats.domain.member.domain.Admin;
import com.flab.woowahaneats.domain.notification.controller.dto.NotificationResponse;
import com.flab.woowahaneats.domain.notification.infrastructure.SseEmitterManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SseEmitterManager sseEmitterManager;

    public SseEmitter subscribe() {
        Admin admin = AuthContextHolder.getContext().getAdmin();
        String emitterId = "ADMIN_" + admin.getId();

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

    public void sendToRole(String role, String message, Long restaurantId) {
        NotificationResponse response = NotificationResponse.of(message, restaurantId);
        sseEmitterManager.sendToRole(role, response);
    }
}