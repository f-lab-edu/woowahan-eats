package com.flab.woowahaneats.domain.order.owner.domain;

import com.flab.woowahaneats.domain.common.vo.Address;
import com.flab.woowahaneats.domain.order.common.OrderMenu;
import com.flab.woowahaneats.domain.order.common.OrderPrice;
import com.flab.woowahaneats.domain.order.common.OrderRequest;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder(toBuilder = true)
public class OwnerOrder {
    private UUID id;
    private Long restaurantId;
    private OrderRequest orderRequest;
    private Address deliveryAddress;
    private OrderPrice orderPrice;
    private List<OrderMenu> orderMenus;
    private UUID userOrderId;
    private UUID riderId;
    private LocalDateTime createdAt;
}
