package com.flab.woowahaneats.domain.order.event;

import java.util.UUID;

public record OwnerOrderCookingCompletedEvent(UUID userOrderId) {
}
