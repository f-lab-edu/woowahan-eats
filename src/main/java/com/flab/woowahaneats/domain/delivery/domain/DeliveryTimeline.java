package com.flab.woowahaneats.domain.delivery.domain;

import java.time.LocalDateTime;

public record DeliveryTimeline(
        LocalDateTime assignedAt,
        LocalDateTime pickedUpAt,
        LocalDateTime deliveredAt
) {
}