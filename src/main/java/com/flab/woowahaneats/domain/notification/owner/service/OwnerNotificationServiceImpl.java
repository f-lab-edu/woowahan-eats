package com.flab.woowahaneats.domain.notification.owner.service;

import com.flab.woowahaneats.domain.auth.AuthContextHolder;
import com.flab.woowahaneats.domain.notification.owner.domain.OwnerNotification;
import com.flab.woowahaneats.domain.notification.infrastructure.SseEmitterManager;
import com.flab.woowahaneats.domain.notification.owner.controller.dto.OwnerNotificationResponse;
import com.flab.woowahaneats.domain.notification.owner.repository.OwnerNotificationRepository;
import com.flab.woowahaneats.domain.owner.domain.Owner;
import com.flab.woowahaneats.domain.restaurant.domain.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OwnerNotificationServiceImpl implements OwnerNotificationService {

    private final OwnerNotificationRepository ownerNotificationRepository;
    private final SseEmitterManager sseEmitterManager;

    @Override
    @Transactional
    public void notify(Owner owner, String message, Restaurant restaurant) {
        OwnerNotification notification = OwnerNotification.create(message, restaurant, owner);
        ownerNotificationRepository.save(notification);

        OwnerNotificationResponse response = OwnerNotificationResponse.from(notification);
        sseEmitterManager.sendToId("OWNER_" + owner.getId(), response);
    }

    @Override
    @Transactional
    public List<OwnerNotificationResponse> getUnreadNotifications() {
        Owner owner = AuthContextHolder.getContext().getOwner();

        List<OwnerNotification> notifications = ownerNotificationRepository.findByOwnerAndReadFalseOrderByCreatedAtDesc(owner);
        notifications.forEach(OwnerNotification::markAsRead);
        return notifications.stream()
                .map(OwnerNotificationResponse::from)
                .toList();
    }

    @Override
    public SseEmitter subscribe() {
        Owner owner = AuthContextHolder.getContext().getOwner();
        String emitterId = "OWNER_" + owner.getId();

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
