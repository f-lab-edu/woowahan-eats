package com.flab.woowahaneats.domain.common.vo;

import jakarta.persistence.Embeddable;

@Embeddable
public record Location(
        double latitude,
        double longitude
) {
}
