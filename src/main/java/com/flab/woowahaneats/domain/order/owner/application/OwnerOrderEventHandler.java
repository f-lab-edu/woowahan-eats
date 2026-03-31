package com.flab.woowahaneats.domain.order.owner.application;

import com.flab.woowahaneats.domain.order.event.PaymentCompletedEvent;
import com.flab.woowahaneats.domain.order.event.UserOrderCancelledEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OwnerOrderEventHandler {

    private final OwnerOrderService ownerOrderService;

    @EventListener
    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        ownerOrderService.createOrder(
                event.getUserOrderId(),
                event.getRestaurantId(),
                event.getOrderMenus(),
                event.getOrderRequest(),
                event.getOrderPrice(),
                event.getDeliveryAddress(),
                event.getCreatedAt()
        );
    }

    @EventListener
    public void handleUserOrderCancelled(UserOrderCancelledEvent event) {
        ownerOrderService.cancelOrder(event.getUserOrderId());
    }
}