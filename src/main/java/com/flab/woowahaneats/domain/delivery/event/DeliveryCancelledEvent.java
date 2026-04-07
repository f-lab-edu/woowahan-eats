package com.flab.woowahaneats.domain.delivery.event;

public record DeliveryCancelledEvent(
        Long deliveryId,
        Long orderId,
        Long riderId
) {
}
