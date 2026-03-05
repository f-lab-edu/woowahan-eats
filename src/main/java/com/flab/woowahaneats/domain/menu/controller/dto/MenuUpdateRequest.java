package com.flab.woowahaneats.domain.menu.controller.dto;

public record MenuUpdateRequest(
        String internalName,
        String displayName,
        String description,
        String imageUrl,
        Integer price,
        Boolean available
) {
}
