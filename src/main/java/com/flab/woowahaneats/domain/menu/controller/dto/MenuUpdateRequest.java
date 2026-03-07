package com.flab.woowahaneats.domain.menu.controller.dto;

import com.flab.woowahaneats.domain.menu.domain.MenuImage;

import java.util.List;

public record MenuUpdateRequest(
        String internalName,
        String displayName,
        String description,
        List<MenuImage> images,
        Integer price,
        Boolean available
) {
}
