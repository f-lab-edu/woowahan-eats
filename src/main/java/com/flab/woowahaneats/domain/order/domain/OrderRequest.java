package com.flab.woowahaneats.domain.order.domain;

public record OrderRequest(
        String requestToStore,
        String requestToRider
) {
}
