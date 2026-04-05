package com.flab.woowahaneats.domain.menu.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public record MenuImage(
        String url,
        int displayOrder,
        boolean main
) {}
