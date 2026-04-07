package com.flab.woowahaneats.domain.payment.application;

import com.flab.woowahaneats.domain.order.user.domain.UserOrder;
import com.flab.woowahaneats.domain.payment.domain.Payment;
import com.flab.woowahaneats.domain.payment.domain.PaymentProvider;

public interface PaymentService {

    Payment preparePayment(UserOrder order, int amount, PaymentProvider provider);

    void confirmPayment(String paymentKey, String gatewayOrderId, int amount);

    void refundPayment(Long orderId, String reason);
}
