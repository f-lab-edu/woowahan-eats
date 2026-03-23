package com.flab.woowahaneats.domain.order.user.application;

import com.flab.woowahaneats.domain.order.event.OrderAcceptedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserOrderEventHandler {

    private final UserOrderService userOrderService;

    @EventListener
    public void handleOrderAccepted(OrderAcceptedEvent event) {
        userOrderService.approveOrder(event.getUserOrderId());
    }
}