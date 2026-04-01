package com.flab.woowahaneats.domain.order.event;

import com.flab.woowahaneats.domain.common.vo.Address;
import com.flab.woowahaneats.domain.order.common.OrderMenu;
import com.flab.woowahaneats.domain.order.common.OrderPrice;
import com.flab.woowahaneats.domain.order.common.OrderRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PaymentCompletedEvent(
        UUID userOrderId,
        Long userId,
        Long restaurantId,
        List<OrderMenu> orderMenus,
        OrderRequest orderRequest,
        OrderPrice orderPrice,
        Address deliveryAddress,
        LocalDateTime createdAt
) {
}
