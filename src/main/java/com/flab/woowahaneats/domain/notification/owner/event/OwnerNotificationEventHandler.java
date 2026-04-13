package com.flab.woowahaneats.domain.notification.owner.event;

import com.flab.woowahaneats.domain.notification.infrastructure.SseEmitterManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class OwnerNotificationEventHandler {

    private final SseEmitterManager sseEmitterManager;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOwnerNotification(OwnerNotificationEvent event) {
        sseEmitterManager.sendToId("OWNER_" + event.ownerId(), event.response());
    }
}