package com.flab.woowahaneats.domain.delivery.controller.dto;

import java.util.UUID;

public record AcceptDeliveryRequest(
        UUID riderId
) {
}