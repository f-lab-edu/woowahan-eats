package com.flab.woowahaneats.domain.order.event;

import com.flab.woowahaneats.domain.common.vo.Address;
import com.flab.woowahaneats.domain.order.common.OrderMenu;
import com.flab.woowahaneats.domain.order.common.OrderPrice;
import com.flab.woowahaneats.domain.order.common.OrderRequest;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
public class UserOrderCreatedEvent extends ApplicationEvent {
    private final UUID userOrderId;
    private final Long restaurantId;
    private final List<OrderMenu> orderMenus;
    private final OrderRequest orderRequest;
    private final OrderPrice orderPrice;
    private final Address deliveryAddress;
    private final LocalDateTime createdAt;

    public UserOrderCreatedEvent(
            Object source,
            UUID userOrderId,
            Long restaurantId,
            List<OrderMenu> orderMenus,
            OrderRequest orderRequest,
            OrderPrice orderPrice,
            Address deliveryAddress,
            LocalDateTime createdAt
    ) {
        super(source);
        this.userOrderId = userOrderId;
        this.restaurantId = restaurantId;
        this.orderMenus = orderMenus;
        this.orderRequest = orderRequest;
        this.orderPrice = orderPrice;
        this.deliveryAddress = deliveryAddress;
        this.createdAt = createdAt;
    }
}