package com.flab.woowahaneats.domain.order.owner.event;

import java.util.UUID;

public record OwnerOrderAcceptedEvent(UUID userOrderId) {
}
