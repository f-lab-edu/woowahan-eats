package com.flab.woowahaneats.domain.order.owner.event;

import java.util.UUID;

public record OwnerOrderCookingStartedEvent(UUID userOrderId) {
}
