package com.flab.woowahaneats.infrastructure.payment;

import com.flab.woowahaneats.domain.payment.domain.PaymentProvider;
import com.flab.woowahaneats.domain.payment.gateway.PaymentApprovalResult;
import com.flab.woowahaneats.domain.payment.gateway.PaymentGateway;
import com.flab.woowahaneats.infrastructure.payment.dto.TossPaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TossPaymentGateway implements PaymentGateway {

    private final TossPaymentClient tossPaymentClient;

    @Override
    public PaymentProvider getProvider() {
        return PaymentProvider.TOSS;
    }

    @Override
    public String generateGatewayOrderId(Long orderId) {
        return "order_" + orderId + "_" + System.currentTimeMillis();
    }

    @Override
    public PaymentApprovalResult confirmPayment(String paymentKey, String orderId, int amount) {
        TossPaymentResponse response = tossPaymentClient.confirmPayment(paymentKey, orderId, amount);
        return new PaymentApprovalResult(response.paymentKey(), response.method());
    }

    @Override
    public void cancelPayment(String paymentKey, String cancelReason) {
        tossPaymentClient.cancelPayment(paymentKey, cancelReason);
    }
}
