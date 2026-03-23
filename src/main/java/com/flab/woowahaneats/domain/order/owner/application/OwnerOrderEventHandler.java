package com.flab.woowahaneats.domain.order.owner.application;

import com.flab.woowahaneats.domain.order.event.UserOrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OwnerOrderEventHandler {

    private final OwnerOrderService ownerOrderService;

    @EventListener
    public void handleUserOrderCreated(UserOrderCreatedEvent event) {
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
}