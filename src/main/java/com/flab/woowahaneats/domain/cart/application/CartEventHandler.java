package com.flab.woowahaneats.domain.cart.application;

import com.flab.woowahaneats.domain.order.user.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class CartEventHandler {

    private final CartService cartService;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleOrderCreated(OrderCreatedEvent event) {
            cartService.deleteCartBySystem(event.cartId());
    }
}
