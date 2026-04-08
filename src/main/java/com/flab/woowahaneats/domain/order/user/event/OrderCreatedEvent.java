package com.flab.woowahaneats.domain.order.user.event;

public record OrderCreatedEvent(
        Long userOrderId,
        Long cartId
) {
}
