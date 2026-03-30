package com.flab.woowahaneats.domain.delivery.domain;

import com.flab.woowahaneats.domain.delivery.exception.InvalidDeliveryStatusException;
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

    public void cancel() {
        if (this.status == DeliveryStatus.DELIVERED) {
            throw new InvalidDeliveryStatusException("완료된 배달은 취소할 수 없습니다.");
        }
        if (this.status == DeliveryStatus.CANCELLED) {
            throw new InvalidDeliveryStatusException("이미 취소된 배달입니다.");
        }
        this.status = DeliveryStatus.CANCELLED;
    }
}
