package com.flab.woowahaneats.domain.payment.controller.dto;

public record PaymentConfirmRequest(
        String paymentKey,
        String orderId,
        int amount
) {
}