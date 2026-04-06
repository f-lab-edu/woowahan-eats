package com.flab.woowahaneats.domain.delivery.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class DeliveryAcceptedEvent extends ApplicationEvent {
    private final Long deliveryId;
    private final Long orderId;
    private final Long riderId;

    public DeliveryAcceptedEvent(Object source, Long deliveryId, Long orderId, Long riderId) {
        super(source);
        this.deliveryId = deliveryId;
        this.orderId = orderId;
        this.riderId = riderId;
    }
}