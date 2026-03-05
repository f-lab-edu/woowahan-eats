package com.flab.woowahaneats.domain.menu.controller.dto;

public record MenuUpdateRequest(
        String name,
        String description,
        String imageUrl,
        Integer price,
        Boolean available
) {
}
