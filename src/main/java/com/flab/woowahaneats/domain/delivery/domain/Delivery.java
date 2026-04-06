package com.flab.woowahaneats.domain.delivery.domain;

import com.flab.woowahaneats.domain.delivery.exception.InvalidDeliveryStatusException;
import com.flab.woowahaneats.domain.order.user.domain.UserOrder;
import com.flab.woowahaneats.domain.rider.domain.Rider;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "deliveries")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private UserOrder order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rider_id")
    private Rider rider;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus status;

    @Embedded
    private DeliveryTimeline timeline;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public static Delivery create(UserOrder order) {
        return Delivery.builder()
                .order(order)
                .status(DeliveryStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public void accept(Rider rider) {
        if (this.status != DeliveryStatus.PENDING) {
            throw new InvalidDeliveryStatusException("대기 중인 배달만 수락할 수 있습니다.");
        }
        this.rider = rider;
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

    public void complete() {
        if (this.status != DeliveryStatus.IN_DELIVERY) {
            throw new InvalidDeliveryStatusException("배송 중인 배달만 완료할 수 있습니다.");
        }
        this.status = DeliveryStatus.DELIVERED;
        this.timeline = this.timeline.complete();
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