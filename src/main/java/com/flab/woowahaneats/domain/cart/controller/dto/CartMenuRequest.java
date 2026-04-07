package com.flab.woowahaneats.domain.cart.controller.dto;

import jakarta.validation.constraints.NotNull;

public record CartMenuRequest(
        @NotNull
        Long menuId,

        @NotNull
        int quantity
) {
}
