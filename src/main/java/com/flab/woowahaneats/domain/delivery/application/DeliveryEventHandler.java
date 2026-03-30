package com.flab.woowahaneats.domain.delivery.application;

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
}