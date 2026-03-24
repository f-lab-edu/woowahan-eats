package com.flab.woowahaneats.domain.order.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
public class UserOrderCancelledEvent extends ApplicationEvent {
    private final UUID userOrderId;

    public UserOrderCancelledEvent(Object source, UUID userOrderId) {
        super(source);
        this.userOrderId = userOrderId;
    }
}