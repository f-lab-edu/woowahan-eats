package com.flab.woowahaneats.domain.rider.event;

import com.flab.woowahaneats.domain.delivery.event.DeliveryAcceptedEvent;
import com.flab.woowahaneats.domain.delivery.event.DeliveryCancelledEvent;
import com.flab.woowahaneats.domain.delivery.event.DeliveryCompletedEvent;
import com.flab.woowahaneats.domain.rider.application.RiderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class RiderEventHandler {

    private final RiderService riderService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleDeliveryAccepted(DeliveryAcceptedEvent event) {
        riderService.startDelivering(event.getRiderId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleDeliveryCompleted(DeliveryCompletedEvent event) {
        if (event.getRiderId() != null) {
            riderService.finishDelivering(event.getRiderId());
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleDeliveryCancelled(DeliveryCancelledEvent event) {
        if (event.getRiderId() != null) {
            riderService.finishDelivering(event.getRiderId());
        }
    }
}