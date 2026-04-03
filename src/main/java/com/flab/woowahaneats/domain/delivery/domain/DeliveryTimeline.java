package com.flab.woowahaneats.domain.delivery.domain;

import java.time.LocalDateTime;

public record DeliveryTimeline(
        LocalDateTime assignedAt,
        LocalDateTime pickedUpAt,
        LocalDateTime deliveredAt
) {
    public DeliveryTimeline pickUp() {
        return new DeliveryTimeline(
                this.assignedAt,
                LocalDateTime.now(),
                this.deliveredAt
        );
    }

    public DeliveryTimeline complete() {
        return new DeliveryTimeline(
                this.assignedAt,
                this.pickedUpAt,
                LocalDateTime.now()
        );
    }
}