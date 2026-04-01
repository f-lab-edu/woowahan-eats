package com.flab.woowahaneats.infrastructure.payment;

import com.flab.woowahaneats.domain.payment.domain.PaymentProvider;
import com.flab.woowahaneats.domain.payment.exception.PaymentProviderNotFoundException;
import com.flab.woowahaneats.domain.payment.gateway.PaymentGateway;
import com.flab.woowahaneats.domain.payment.gateway.PaymentGatewayResolver;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class PaymentGatewayResolverImpl implements PaymentGatewayResolver {

    private final Map<PaymentProvider, PaymentGateway> gateways;

    public PaymentGatewayResolverImpl(List<PaymentGateway> gatewayList) {
        this.gateways = gatewayList.stream()
                .collect(Collectors.toMap(
                        PaymentGateway::getProvider,
                        Function.identity()
                ));
    }

    @Override
    public PaymentGateway getGateway(PaymentProvider provider) {
        PaymentGateway gateway = gateways.get(provider);
        if (gateway == null) {
            throw new PaymentProviderNotFoundException();
        }
        return gateway;
    }
}