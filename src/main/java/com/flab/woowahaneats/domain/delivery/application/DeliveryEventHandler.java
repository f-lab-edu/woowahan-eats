package com.flab.woowahaneats.domain.delivery.application;

import com.flab.woowahaneats.domain.delivery.event.DeliveryCancelledEvent;
import com.flab.woowahaneats.domain.delivery.event.DeliveryCompletedEvent;
import com.flab.woowahaneats.domain.delivery.event.DeliveryCreatedEvent;
import com.flab.woowahaneats.domain.order.user.application.UserOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class DeliveryEventHandler {

    private final UserOrderService userOrderService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleDeliveryCreated(DeliveryCreatedEvent event) {
        userOrderService.startDelivering(event.orderId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleDeliveryCancelled(DeliveryCancelledEvent event) {
        userOrderService.resetOrderToReady(event.orderId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleDeliveryCompleted(DeliveryCompletedEvent event) {
        userOrderService.completeOrder(event.orderId());
    }
}
