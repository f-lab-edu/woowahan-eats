package com.flab.woowahaneats.domain.delivery.event;

public record DeliveryAcceptedEvent(
        Long deliveryId,
        Long orderId,
        Long riderId
) {
}
