package com.flab.woowahaneats.domain.order.owner.application;

import com.flab.woowahaneats.domain.common.vo.Address;
import com.flab.woowahaneats.domain.order.common.OrderMenu;
import com.flab.woowahaneats.domain.order.common.OrderPrice;
import com.flab.woowahaneats.domain.order.common.OrderRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface OwnerOrderService {

    void createOrder(
            Long userOrderId,
            List<OrderMenu> orderMenus,
            OrderRequest orderRequest,
            OrderPrice orderPrice,
            Address deliveryAddress,
            LocalDateTime createdAt
    );

    void approveOrder(Long userOrderId);

    void startCooking(Long userOrderId);

    void completeCooking(Long userOrderId);

    void cancelOrder(Long userOrderId);
}
