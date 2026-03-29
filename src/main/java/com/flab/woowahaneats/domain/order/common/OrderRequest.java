package com.flab.woowahaneats.domain.order.common;

public record OrderRequest(
        String requestToStore,
        String requestToRider
) {
}
