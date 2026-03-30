package com.flab.woowahaneats.domain.delivery.application;

import com.flab.woowahaneats.domain.delivery.event.DeliveryCancelledEvent;
import com.flab.woowahaneats.domain.delivery.event.DeliveryCompletedEvent;
import com.flab.woowahaneats.domain.delivery.event.DeliveryCreatedEvent;
import com.flab.woowahaneats.domain.order.user.application.UserOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeliveryEventHandler {

    private final UserOrderService userOrderService;

    @EventListener
    public void handleDeliveryCreated(DeliveryCreatedEvent event) {
        userOrderService.startDelivering(event.getOrderId());
    }

    @EventListener
    public void handleDeliveryCancelled(DeliveryCancelledEvent event) {
        userOrderService.resetOrderToReady(event.getOrderId());
    }

    @EventListener
    public void handleDeliveryCompleted(DeliveryCompletedEvent event) {
        userOrderService.completeOrder(event.getOrderId());
    }
}