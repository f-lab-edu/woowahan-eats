package com.flab.woowahaneats.domain.payment.gateway;

import com.flab.woowahaneats.domain.payment.domain.PaymentProvider;

import java.util.UUID;

public interface PaymentGateway {

    PaymentProvider getProvider();
    String generateGatewayOrderId(UUID orderId);
    PaymentApprovalResult confirmPayment(String paymentKey, String orderId, int amount);
    void cancelPayment(String paymentKey, String cancelReason);
}