package com.flab.woowahaneats.domain.delivery.application;

import com.flab.woowahaneats.domain.order.user.application.UserOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeliveryEventHandler {

    private final UserOrderService userOrderService;
}
