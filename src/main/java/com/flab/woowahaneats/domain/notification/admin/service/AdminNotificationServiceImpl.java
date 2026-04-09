package com.flab.woowahaneats.domain.notification.admin.service;

import com.flab.woowahaneats.domain.auth.AuthContextHolder;
import com.flab.woowahaneats.domain.admin.domain.Admin;
import com.flab.woowahaneats.domain.notification.admin.controller.dto.AdminNotificationResponse;
import com.flab.woowahaneats.domain.notification.admin.domain.AdminNotification;
import com.flab.woowahaneats.domain.notification.infrastructure.SseEmitterManager;
import com.flab.woowahaneats.domain.notification.admin.repository.AdminNotificationRepository;
import com.flab.woowahaneats.domain.owner.domain.Owner;
import com.flab.woowahaneats.domain.restaurant.domain.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminNotificationServiceImpl implements AdminNotificationService {

    private final AdminNotificationRepository adminNotificationRepository;
    private final SseEmitterManager sseEmitterManager;

    @Override
    @Transactional
    public void notify(String message, Restaurant restaurant, Owner owner) {
        AdminNotification notification = AdminNotification.create(message, restaurant, owner);
        adminNotificationRepository.save(notification);

        AdminNotificationResponse response = AdminNotificationResponse.from(notification);
        sseEmitterManager.sendToRole("ADMIN", response);
    }

    @Override
    @Transactional
    public List<AdminNotificationResponse> getUnreadNotifications() {
        List<AdminNotification> notifications = adminNotificationRepository.findByReadFalseOrderByCreatedAtDesc();
        notifications.forEach(AdminNotification::markAsRead);
        return notifications.stream()
                .map(AdminNotificationResponse::from)
                .toList();
    }

    @Override
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
}
