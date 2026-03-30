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

    public void accept(UUID riderId) {
        if (this.status != DeliveryStatus.PENDING) {
            throw new InvalidDeliveryStatusException("대기 중인 배달만 수락할 수 있습니다.");
        }
        this.riderId = riderId;
        this.status = DeliveryStatus.ASSIGNED;
        this.timeline = new DeliveryTimeline(LocalDateTime.now(), null, null);
    }

    public void startPickup() {
        if (this.status != DeliveryStatus.ASSIGNED) {
            throw new InvalidDeliveryStatusException("배정된 배달만 픽업을 시작할 수 있습니다.");
        }
        this.status = DeliveryStatus.PICKING_UP;
    }

    public void completePickup() {
        if (this.status != DeliveryStatus.PICKING_UP) {
            throw new InvalidDeliveryStatusException("픽업 이동 중인 배달만 픽업을 완료할 수 있습니다.");
        }
        this.status = DeliveryStatus.PICKED_UP;
        this.timeline = this.timeline.pickUp();
    }

    public void startDelivery() {
        if (this.status != DeliveryStatus.PICKED_UP) {
            throw new InvalidDeliveryStatusException("픽업 완료된 배달만 배송을 시작할 수 있습니다.");
        }
        this.status = DeliveryStatus.IN_DELIVERY;
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
