package com.flab.woowahaneats.domain.notification.admin.event;

import com.flab.woowahaneats.domain.notification.infrastructure.SseEmitterManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class AdminNotificationEventHandler {

    private final SseEmitterManager sseEmitterManager;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleAdminNotification(AdminNotificationEvent event) {
        sseEmitterManager.sendToRole("ADMIN", event.response());
    }
}