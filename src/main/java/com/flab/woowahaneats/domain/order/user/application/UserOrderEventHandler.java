package com.flab.woowahaneats.domain.order.user.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserOrderEventHandler {

    private final UserOrderService userOrderService;
}
