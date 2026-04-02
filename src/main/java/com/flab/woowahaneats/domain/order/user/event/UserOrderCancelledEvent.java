package com.flab.woowahaneats.domain.order.user.event;

import java.util.UUID;

public record UserOrderCancelledEvent(UUID userOrderId) {
}
