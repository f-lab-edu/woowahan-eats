package com.flab.woowahaneats.domain.delivery.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder(toBuilder = true)
public class Delivery {

    private UUID id;
    private UUID orderId;
    private UUID riderId;
    private DeliveryStatus status;
    private DeliveryTimeline timeline;
    private LocalDateTime createdAt;

    public static Delivery create(UUID orderId) {
        return Delivery.builder()
                .id(UUID.randomUUID())
                .orderId(orderId)
                .status(DeliveryStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
