package com.flab.woowahaneats.domain.order.event;

import java.util.UUID;

public record OwnerOrderCookingStartedEvent(UUID userOrderId) {
}
