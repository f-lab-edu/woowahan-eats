package com.flab.woowahaneats.domain.menu.domain;

public record MenuImage(
        String url,
        int displayOrder,
        boolean main
) {}

