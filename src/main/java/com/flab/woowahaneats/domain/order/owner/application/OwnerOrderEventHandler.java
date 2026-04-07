package com.flab.woowahaneats.domain.order.owner.application;

import com.flab.woowahaneats.domain.order.user.event.UserOrderCancelledEvent;
import com.flab.woowahaneats.domain.payment.event.PaymentCompletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class OwnerOrderEventHandler {

    private final OwnerOrderService ownerOrderService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        ownerOrderService.createOrder(
                event.userOrderId(),
                event.orderMenus(),
                event.orderRequest(),
                event.orderPrice(),
                event.deliveryAddress(),
                event.createdAt()
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserOrderCancelled(UserOrderCancelledEvent event) {
        ownerOrderService.cancelOrder(event.userOrderId());
    }
}
