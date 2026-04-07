package com.flab.woowahaneats.domain.cart.controller.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateCartRequest(
        @Valid
        @NotEmpty
        List<CartMenuRequest> menus
) {
}
