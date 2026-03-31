package com.flab.woowahaneats.domain.payment.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record PaymentConfirmRequest(

        @NotBlank
        String paymentKey,

        @NotBlank
        String orderId,

        @Positive
        int amount
) {
}
