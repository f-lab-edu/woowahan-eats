package com.flab.woowahaneats.domain.order.controller.dto;

import com.flab.woowahaneats.domain.common.vo.Address;
import com.flab.woowahaneats.domain.order.domain.Order;
import com.flab.woowahaneats.domain.order.domain.OrderMenu;
import com.flab.woowahaneats.domain.order.domain.OrderPrice;
import com.flab.woowahaneats.domain.order.domain.OrderRequest;
import com.flab.woowahaneats.domain.order.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID orderId,
        Long restaurantId,
        OrderStatus status,
        List<OrderMenu> orderMenus,
        OrderPrice orderPrice,
        OrderRequest orderRequest,
        Address deliveryAddress,
        LocalDateTime createdAt
) {
    public static OrderResponse from(Order order) {
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