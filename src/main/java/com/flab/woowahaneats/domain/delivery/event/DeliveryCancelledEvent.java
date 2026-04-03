package com.flab.woowahaneats.domain.delivery.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
public class DeliveryCancelledEvent extends ApplicationEvent {
    private final UUID deliveryId;
    private final UUID orderId;
    private final Long riderId;

    public DeliveryCancelledEvent(Object source, UUID deliveryId, UUID orderId, Long riderId) {
        super(source);
        this.deliveryId = deliveryId;
        this.orderId = orderId;
        this.riderId = riderId;
    }
}