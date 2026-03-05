package com.flab.woowahaneats.domain.menu.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MenuRequest (

        @NotNull
        Long id,

        @NotBlank
        String internalName,

        @NotBlank
        String displayName,

        String description,

        String imageUrl,

        @Min(0)
        int price,

        boolean available
){
}
