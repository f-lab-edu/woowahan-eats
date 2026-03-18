package com.flab.woowahaneats.domain.order.domain;

import com.flab.woowahaneats.domain.common.vo.Address;
import com.flab.woowahaneats.domain.order.application.exception.MinOrderAmountNotMetException;
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

    public static Order create(
            Long userId,
            Long restaurantId,
            List<OrderMenu> orderMenus,
            Address deliveryAddress,
            String requestToStore,
            String requestToRider,
            int deliveryFee,
            int minOrderAmt
    ) {
        int menuTotalPrice = calculateMenuTotalPrice(orderMenus);
        validateMinOrderAmount(menuTotalPrice, minOrderAmt);

        return Order.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .restaurantId(restaurantId)
                .riderId(null)
                .orderMenus(orderMenus)
                .deliveryAddress(deliveryAddress)
                .requestToStore(requestToStore)
                .requestToRider(requestToRider)
                .menuTotalPrice(menuTotalPrice)
                .deliveryFee(deliveryFee)
                .totalPrice(menuTotalPrice + deliveryFee)
                .status(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .completedAt(null)
                .build();
    }

    private static void validateMinOrderAmount(int menuTotalPrice, int minOrderAmt) {
        if (menuTotalPrice < minOrderAmt) {
            throw new MinOrderAmountNotMetException(minOrderAmt);
        }
    }

    private static int calculateMenuTotalPrice(List<OrderMenu> orderMenus) {
        return orderMenus.stream()
                .mapToInt(menu -> menu.price() * menu.quantity())
                .sum();
    }
}
