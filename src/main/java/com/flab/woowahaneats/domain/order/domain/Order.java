package com.flab.woowahaneats.domain.order.domain;

import com.flab.woowahaneats.domain.common.vo.Address;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder(toBuilder = true)
public class Order {
    private UUID id;
    private Long userId;
    private Long restaurantId;
    private UUID riderId;
    private List<OrderMenu> orderMenus;
    private String requestToStore;
    private String requestToRider;
    private OrderStatus status;
    private int menuTotalPrice;
    private int totalPrice;
    private int deliveryFee;
    private Address deliveryAddress;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
}
