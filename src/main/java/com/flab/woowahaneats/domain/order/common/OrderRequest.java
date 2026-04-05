package com.flab.woowahaneats.domain.order.common;

import jakarta.persistence.Embeddable;

@Embeddable
public record OrderRequest(
        String requestToStore,
        String requestToRider
) {
}
