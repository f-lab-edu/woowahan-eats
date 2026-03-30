package com.flab.woowahaneats.domain.payment.gateway;

public interface PaymentGateway {
    PaymentApprovalResult confirmPayment(String paymentKey, String orderId, int amount);
    void cancelPayment(String paymentKey, String cancelReason);
}