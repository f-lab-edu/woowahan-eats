package com.flab.woowahaneats.domain.delivery.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
public class DeliveryCompletedEvent extends ApplicationEvent {
    private final UUID deliveryId;
    private final UUID orderId;

    public DeliveryCompletedEvent(Object source, UUID deliveryId, UUID orderId) {
        super(source);
        this.deliveryId = deliveryId;
        this.orderId = orderId;
    }
}