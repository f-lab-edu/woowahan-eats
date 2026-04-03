package com.flab.woowahaneats.domain.order.user.application;

import com.flab.woowahaneats.domain.order.owner.event.OwnerOrderAcceptedEvent;
import com.flab.woowahaneats.domain.order.owner.event.OwnerOrderCookingCompletedEvent;
import com.flab.woowahaneats.domain.order.owner.event.OwnerOrderCookingStartedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserOrderEventHandler {

    private final UserOrderService userOrderService;

    @EventListener
    public void handleOrderAccepted(OwnerOrderAcceptedEvent event) {
        userOrderService.approveOrder(event.userOrderId());
    }

    @EventListener
    public void handleCookingStarted(OwnerOrderCookingStartedEvent event) {
        userOrderService.startCooking(event.userOrderId());
    }

    @EventListener
    public void handleCookingCompleted(OwnerOrderCookingCompletedEvent event) {
        userOrderService.completeCooking(event.userOrderId());
    }
}
