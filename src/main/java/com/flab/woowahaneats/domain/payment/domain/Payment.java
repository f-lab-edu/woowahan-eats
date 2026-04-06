package com.flab.woowahaneats.domain.payment.domain;

import com.flab.woowahaneats.domain.order.user.domain.UserOrder;
import com.flab.woowahaneats.domain.payment.exception.InvalidPaymentStatusException;
import com.flab.woowahaneats.domain.payment.exception.PaymentAmountMismatchException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private UserOrder order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentProvider provider;

    @Column(name = "payment_key")
    private String paymentKey;

    @Column(name = "gateway_order_id", nullable = false)
    private String gatewayOrderId;

    @Column(nullable = false)
    private int amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    private String method;

    @Column(name = "requested_at", nullable = false)
    private LocalDateTime requestedAt;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "fail_reason")
    private String failReason;

    public static Payment prepare(UserOrder order, int amount, PaymentProvider provider, String gatewayOrderId) {
        return Payment.builder()
                .order(order)
                .provider(provider)
                .gatewayOrderId(gatewayOrderId)
                .amount(amount)
                .status(PaymentStatus.PENDING)
                .requestedAt(LocalDateTime.now())
                .build();
    }

    public void approve(String paymentKey, String method) {
        if (this.status != PaymentStatus.PENDING) {
            throw new InvalidPaymentStatusException("결제 대기 상태만 승인할 수 있습니다.");
        }
        this.paymentKey = paymentKey;
        this.method = method;
        this.status = PaymentStatus.COMPLETED;
        this.approvedAt = LocalDateTime.now();
    }

    public void fail(String reason) {
        this.status = PaymentStatus.FAILED;
        this.failReason = reason;
    }

    public void refund() {
        if (this.status != PaymentStatus.COMPLETED) {
            throw new InvalidPaymentStatusException("완료된 결제만 환불할 수 있습니다.");
        }
        this.status = PaymentStatus.REFUNDED;
    }

    public void validateAmount(int amount) {
        if (this.amount != amount) {
            throw new PaymentAmountMismatchException();
        }
    }
}