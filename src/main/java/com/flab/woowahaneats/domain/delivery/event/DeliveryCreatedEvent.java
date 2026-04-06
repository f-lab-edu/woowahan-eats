package com.flab.woowahaneats.domain.delivery.event;

public record DeliveryCreatedEvent(
        Long deliveryId,
        Long orderId
) {
}
