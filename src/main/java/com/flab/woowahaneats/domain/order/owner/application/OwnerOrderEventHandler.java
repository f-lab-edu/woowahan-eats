package com.flab.woowahaneats.domain.order.owner.application;

import com.flab.woowahaneats.domain.order.event.PaymentCompletedEvent;
import com.flab.woowahaneats.domain.order.event.UserOrderCancelledEvent;
import com.flab.woowahaneats.domain.order.user.domain.UserOrder;
import com.flab.woowahaneats.domain.order.user.repository.UserOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OwnerOrderEventHandler {

    private final OwnerOrderService ownerOrderService;
    private final UserOrderRepository userOrderRepository;

    @EventListener
    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        UserOrder userOrder = userOrderRepository.findById(event.getUserOrderId())
                .orElseThrow();

        ownerOrderService.createOrder(
                userOrder.getId(),
                userOrder.getRestaurantId(),
                userOrder.getOrderMenus(),
                userOrder.getOrderRequest(),
                userOrder.getOrderPrice(),
                userOrder.getDeliveryAddress(),
                userOrder.getCreatedAt()
        );
    }

    @EventListener
    public void handleUserOrderCancelled(UserOrderCancelledEvent event) {
        ownerOrderService.cancelOrder(event.getUserOrderId());
    }
}