package com.flab.woowahaneats.domain.order.user.controller.dto;

import com.flab.woowahaneats.domain.common.vo.Address;
import com.flab.woowahaneats.domain.payment.domain.PaymentProvider;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateOrderRequest(
        @NotNull
        UUID cartId,

        @Valid
        @NotNull
        Address deliveryAddress,

        String requestToStore,

        String requestToRider,

        @NotNull
        PaymentProvider paymentProvider
) {
}