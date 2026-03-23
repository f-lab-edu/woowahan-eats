package com.flab.woowahaneats.domain.order.user.domain;

import com.flab.woowahaneats.domain.common.vo.Address;
import com.flab.woowahaneats.domain.order.exception.MinOrderAmountNotMetException;
import com.flab.woowahaneats.domain.order.exception.OrderCannotBeApprovedException;
import com.flab.woowahaneats.domain.order.exception.OrderCannotBeCancelledException;
import com.flab.woowahaneats.domain.order.common.OrderMenu;
import com.flab.woowahaneats.domain.order.common.OrderPrice;
import com.flab.woowahaneats.domain.order.common.OrderRequest;
import com.flab.woowahaneats.domain.order.common.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder(toBuilder = true)
public class UserOrder {
    private UUID id;
    private Long userId;
    private Long restaurantId;
    private List<OrderMenu> orderMenus;
    private OrderRequest orderRequest;
    private OrderStatus status;
    private OrderPrice orderPrice;
    private Address deliveryAddress;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

    public static UserOrder create(
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

        return UserOrder.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .restaurantId(restaurantId)
                .orderMenus(orderMenus)
                .deliveryAddress(deliveryAddress)
                .orderRequest(new OrderRequest(requestToStore, requestToRider))
                .orderPrice(OrderPrice.of(menuTotalPrice, deliveryFee))
                .status(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .completedAt(null)
                .build();
    }

    public void cancel(){
        validateCancellable();
        this.status = OrderStatus.CANCELLED;
        this.completedAt = LocalDateTime.now();
    }

    public void approve() {
        validateApprovable();
        this.status = OrderStatus.ACCEPTED;
    }

    public boolean isActive() {
        return this.status != OrderStatus.CANCELLED
                && this.status != OrderStatus.COMPLETED;
    }

    private void validateCancellable() {
        if (this.status == OrderStatus.CANCELLED) {
            throw new OrderCannotBeCancelledException("이미 취소된 주문입니다.");
        }
        if (this.status == OrderStatus.COMPLETED) {
            throw new OrderCannotBeCancelledException("완료된 주문은 취소할 수 없습니다.");
        }
        if (this.status == OrderStatus.DELIVERING) {
            throw new OrderCannotBeCancelledException("배달 중인 주문은 취소할 수 없습니다.");
        }
    }

    private void validateApprovable() {
        if (this.status != OrderStatus.PENDING) {
            throw new OrderCannotBeApprovedException("대기 중인 주문만 승인할 수 있습니다.");
        }
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
