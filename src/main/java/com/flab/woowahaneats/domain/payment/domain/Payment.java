package com.flab.woowahaneats.domain.payment.domain;

import com.flab.woowahaneats.domain.payment.exception.InvalidPaymentStatusException;
import com.flab.woowahaneats.domain.payment.exception.PaymentAmountMismatchException;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder(toBuilder = true)
public class Payment {
    private UUID id;
    private UUID orderId;
    private String paymentKey;
    private String tossOrderId;
    private int amount;
    private PaymentStatus status;
    private String method;
    private LocalDateTime requestedAt;
    private LocalDateTime approvedAt;
    private String failReason;

    public static Payment prepare(UUID orderId, int amount) {
        return Payment.builder()
                .id(UUID.randomUUID())
                .orderId(orderId)
                .tossOrderId(orderId.toString())
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