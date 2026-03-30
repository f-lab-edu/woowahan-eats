package com.flab.woowahaneats.domain.order.user.controller.dto;

public record CreateOrderResponse(
        String orderId,
        int amount
) {
}