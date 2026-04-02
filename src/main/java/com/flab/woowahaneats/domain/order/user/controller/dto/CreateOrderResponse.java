package com.flab.woowahaneats.domain.order.user.controller.dto;

import com.flab.woowahaneats.domain.payment.domain.PaymentProvider;
import com.flab.woowahaneats.domain.payment.domain.PaymentStatus;

public record CreateOrderResponse(
        String orderId,
        int amount,
        String orderName,
        PaymentProvider paymentProvider,
        PaymentStatus paymentStatus
) {
}
