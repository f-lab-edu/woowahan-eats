package com.flab.woowahaneats.domain.delivery.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
public class DeliveryCreatedEvent extends ApplicationEvent {
    private final UUID deliveryId;
    private final Long orderId;

    public DeliveryCreatedEvent(Object source, UUID deliveryId, Long orderId) {
        super(source);
        this.deliveryId = deliveryId;
        this.orderId = orderId;
    }
}