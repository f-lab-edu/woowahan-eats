package com.flab.woowahaneats.domain.delivery.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class DeliveryCreatedEvent extends ApplicationEvent {
    private final Long deliveryId;
    private final Long orderId;

    public DeliveryCreatedEvent(Object source, Long deliveryId, Long orderId) {
        super(source);
        this.deliveryId = deliveryId;
        this.orderId = orderId;
    }
}