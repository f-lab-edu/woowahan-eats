package com.flab.woowahaneats.domain.order.user.domain;

import com.flab.woowahaneats.domain.common.vo.Address;
import com.flab.woowahaneats.domain.order.exception.InvalidOrderStatusException;
import com.flab.woowahaneats.domain.order.exception.MinOrderAmountNotMetException;
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
public class UserOrder {
    private UUID id;
    private Long userId;
    private Long restaurantId;
    private List<OrderMenu> orderMenus;
    private OrderRequest orderRequest;
    private UserOrderStatus status;
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
                .status(UserOrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .completedAt(null)
                .build();
    }

    public void cancel(){
        if (this.status == UserOrderStatus.CANCELLED) {
            throw new InvalidOrderStatusException("이미 취소된 주문입니다.");
        }
        if (this.status == UserOrderStatus.COMPLETED) {
            throw new InvalidOrderStatusException("완료된 주문은 취소할 수 없습니다.");
        }
        if (this.status == UserOrderStatus.READY) {
            throw new InvalidOrderStatusException("조리가 완료된 주문은 취소할 수 없습니다.");
        }
        if (this.status == UserOrderStatus.DELIVERING) {
            throw new InvalidOrderStatusException("배달 중인 주문은 취소할 수 없습니다.");
        }
        this.status = UserOrderStatus.CANCELLED;
        this.completedAt = LocalDateTime.now();
    }

    public void approve() {
        if (this.status != UserOrderStatus.PENDING) {
            throw new InvalidOrderStatusException("대기 중인 주문만 승인할 수 있습니다.");
        }
        this.status = UserOrderStatus.ACCEPTED;
    }

    public void startCooking() {
        if (this.status != UserOrderStatus.ACCEPTED) {
            throw new InvalidOrderStatusException("승인된 주문만 조리를 시작할 수 있습니다.");
        }
        this.status = UserOrderStatus.COOKING;
    }

    public void completeCooking() {
        if (this.status != UserOrderStatus.COOKING) {
            throw new InvalidOrderStatusException("조리 중인 주문만 조리를 완료할 수 있습니다.");
        }
        this.status = UserOrderStatus.READY;
    }

    public void startDelivering() {
        if (this.status != UserOrderStatus.READY) {
            throw new InvalidOrderStatusException("조리 완료된 주문만 배달을 시작할 수 있습니다.");
        }
        this.status = UserOrderStatus.DELIVERING;
    }

    public void complete() {
        if (this.status != UserOrderStatus.DELIVERING) {
            throw new InvalidOrderStatusException("배달 중인 주문만 완료할 수 있습니다.");
        }
        this.status = UserOrderStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }

    public boolean isActive() {
        return this.status != UserOrderStatus.CANCELLED
                && this.status != UserOrderStatus.COMPLETED;
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
