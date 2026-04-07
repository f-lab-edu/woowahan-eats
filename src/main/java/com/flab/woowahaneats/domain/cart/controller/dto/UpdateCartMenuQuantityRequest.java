package com.flab.woowahaneats.domain.cart.controller.dto;

import jakarta.validation.constraints.NotNull;

public record UpdateCartMenuQuantityRequest(
        @NotNull
        int quantity
) {
}
