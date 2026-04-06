package com.flab.woowahaneats.domain.delivery.event;

public record DeliveryCompletedEvent(
        Long deliveryId,
        Long orderId,
        Long riderId
) {
}
