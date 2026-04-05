package com.flab.woowahaneats.domain.order.user.controller.dto;

import com.flab.woowahaneats.domain.common.vo.Address;
import com.flab.woowahaneats.domain.order.user.domain.UserOrder;
import com.flab.woowahaneats.domain.order.user.domain.UserOrderStatus;
import com.flab.woowahaneats.domain.order.common.OrderMenu;
import com.flab.woowahaneats.domain.order.common.OrderPrice;
import com.flab.woowahaneats.domain.order.common.OrderRequest;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long orderId,
        Long restaurantId,
        UserOrderStatus status,
        List<OrderMenu> orderMenus,
        OrderPrice orderPrice,
        OrderRequest orderRequest,
        Address deliveryAddress,
        LocalDateTime createdAt
) {
    public static OrderResponse from(UserOrder order) {
        return new OrderResponse(
                order.getId(),
                order.getRestaurantId(),
                order.getStatus(),
                order.getOrderMenus(),
                order.getOrderPrice(),
                order.getOrderRequest(),
                order.getDeliveryAddress(),
                order.getCreatedAt()
        );
    }
}