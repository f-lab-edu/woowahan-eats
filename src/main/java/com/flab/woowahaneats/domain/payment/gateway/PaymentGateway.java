package com.flab.woowahaneats.domain.payment.gateway;

import com.flab.woowahaneats.domain.payment.domain.PaymentProvider;

public interface PaymentGateway {

    PaymentProvider getProvider();
    String generateGatewayOrderId(Long orderId);
    PaymentApprovalResult confirmPayment(String paymentKey, String orderId, int amount);
    void cancelPayment(String paymentKey, String cancelReason);
}