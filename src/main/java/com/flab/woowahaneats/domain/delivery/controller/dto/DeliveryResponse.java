package com.flab.woowahaneats.domain.delivery.controller.dto;

import com.flab.woowahaneats.domain.delivery.domain.Delivery;
import com.flab.woowahaneats.domain.delivery.domain.DeliveryStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record DeliveryResponse(
        UUID deliveryId,
        UUID orderId,
        UUID riderId,
        DeliveryStatus status,
        LocalDateTime createdAt
) {
    public static DeliveryResponse from(Delivery delivery) {
        return new DeliveryResponse(
                delivery.getId(),
                delivery.getOrderId(),
                delivery.getRiderId(),
                delivery.getStatus(),
                delivery.getCreatedAt()
        );
    }
}