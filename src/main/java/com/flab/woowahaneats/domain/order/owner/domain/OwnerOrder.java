package com.flab.woowahaneats.domain.order.owner.domain;

import com.flab.woowahaneats.domain.common.vo.Address;
import com.flab.woowahaneats.domain.order.common.OrderMenu;
import com.flab.woowahaneats.domain.order.common.OrderPrice;
import com.flab.woowahaneats.domain.order.common.OrderRequest;
import com.flab.woowahaneats.domain.order.exception.InvalidOrderStatusException;
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
    private OwnerOrderStatus status;
    private LocalDateTime createdAt;

    public void approve() {
        if (this.status != OwnerOrderStatus.PENDING) {
            throw new InvalidOrderStatusException("대기 중인 주문만 승인할 수 있습니다.");
        }
        this.status = OwnerOrderStatus.ACCEPTED;
    }

    public void startCooking() {
        if (this.status != OwnerOrderStatus.ACCEPTED) {
            throw new InvalidOrderStatusException("승인된 주문만 조리를 시작할 수 있습니다.");
        }
        this.status = OwnerOrderStatus.COOKING;
    }

    public void completeCooking() {
        if (this.status != OwnerOrderStatus.COOKING) {
            throw new InvalidOrderStatusException("조리 중인 주문만 완료할 수 있습니다.");
        }
        this.status = OwnerOrderStatus.READY;
    }

    public void cancel() {
        if (this.status == OwnerOrderStatus.READY) {
            throw new InvalidOrderStatusException("픽업 대기 중인 주문은 취소할 수 없습니다.");
        }
        if (this.status == OwnerOrderStatus.CANCELLED) {
            throw new InvalidOrderStatusException("이미 취소된 주문입니다.");
        }
        this.status = OwnerOrderStatus.CANCELLED;
    }
}
