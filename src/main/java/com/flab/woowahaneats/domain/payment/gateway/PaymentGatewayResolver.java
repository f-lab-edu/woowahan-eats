package com.flab.woowahaneats.domain.payment.gateway;

import com.flab.woowahaneats.domain.payment.domain.PaymentProvider;

public interface PaymentGatewayResolver {

    PaymentGateway getGateway(PaymentProvider provider);
}