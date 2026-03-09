package com.flab.woowahaneats.domain.menu.controller.dto;

import com.flab.woowahaneats.domain.menu.domain.MenuImage;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record MenuRequest (

        @NotNull
        Long id,

        @NotBlank
        String internalName,

        @NotBlank
        String displayName,

        String description,

        List<MenuImage> images,

        @Min(0)
        int price,

        boolean available
){
}
