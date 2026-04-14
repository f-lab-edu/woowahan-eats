package com.flab.woowahaneats.infrastructure.payment;

import com.flab.woowahaneats.domain.payment.domain.PaymentProvider;
import com.flab.woowahaneats.domain.payment.gateway.PaymentApprovalResult;
import com.flab.woowahaneats.domain.payment.gateway.PaymentGateway;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TestPaymentGateway implements PaymentGateway {

    @Override
    public PaymentProvider getProvider() {
        return PaymentProvider.TEST;
    }

    @Override
    public String generateGatewayOrderId(Long orderId) {
        return "TEST_" + orderId;
    }

    @Override
    public PaymentApprovalResult confirmPayment(String paymentKey, String orderId, int amount) {
        return new PaymentApprovalResult(
                "TEST_PAY_" + UUID.randomUUID(),
                "테스트결제"
        );
    }

    @Override
    public void cancelPayment(String paymentKey, String cancelReason) {
        // no-op
    }
}