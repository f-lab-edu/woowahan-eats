package com.flab.woowahaneats.domain.payment.gateway;

public record PaymentApprovalResult(
        String paymentKey,
        String method
) {
}