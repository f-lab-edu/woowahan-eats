package com.flab.woowahaneats.domain.restaurant.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record RestaurantRequest(

        @NotNull
        Long id,

        @NotNull
        Long memberId,

        @NotBlank
        String name,

        @NotBlank
        String description,

        @NotBlank
        String address,

        @NotNull @Min(0)
        int minOrderAmt,

        @NotNull
        BigDecimal deliveryFee,

        @NotBlank
        String region
 ) {
}
