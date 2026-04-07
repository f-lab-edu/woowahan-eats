package com.flab.woowahaneats.domain.delivery.domain;

import jakarta.persistence.Embeddable;

import java.time.LocalDateTime;

@Embeddable
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