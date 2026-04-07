package com.flab.woowahaneats.domain.payment.event;

import com.flab.woowahaneats.domain.common.vo.Address;
import com.flab.woowahaneats.domain.order.common.OrderMenu;
import com.flab.woowahaneats.domain.order.common.OrderPrice;
import com.flab.woowahaneats.domain.order.common.OrderRequest;

import java.time.LocalDateTime;
import java.util.List;

public record PaymentCompletedEvent(
        Long userOrderId,
        Long userId,
        Long restaurantId,
        List<OrderMenu> orderMenus,
        OrderRequest orderRequest,
        OrderPrice orderPrice,
        Address deliveryAddress,
        LocalDateTime createdAt
) {
}
