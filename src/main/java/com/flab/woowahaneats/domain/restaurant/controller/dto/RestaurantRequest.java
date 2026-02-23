package com.flab.woowahaneats.domain.restaurant.controller.dto;

import com.flab.woowahaneats.domain.common.vo.Address;
import com.flab.woowahaneats.domain.common.vo.Location;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RestaurantRequest(

        @NotNull
        Long id,

        @NotBlank
        String name,

        @NotBlank
        String description,

        @Valid
        @NotNull
        Address address,

        @Valid
        @NotNull
        Location location,

        @Min(0)
        int minOrderAmt,

        @NotNull
        int deliveryFee
 ) {
}
