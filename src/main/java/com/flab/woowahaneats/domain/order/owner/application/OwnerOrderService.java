package com.flab.woowahaneats.domain.order.owner.application;

import com.flab.woowahaneats.domain.common.vo.Address;
import com.flab.woowahaneats.domain.order.common.OrderMenu;
import com.flab.woowahaneats.domain.order.common.OrderPrice;
import com.flab.woowahaneats.domain.order.common.OrderRequest;
import com.flab.woowahaneats.domain.order.event.OrderAcceptedEvent;
import com.flab.woowahaneats.domain.order.event.OwnerOrderCookingCompletedEvent;
import com.flab.woowahaneats.domain.order.event.OwnerOrderCookingStartedEvent;
import com.flab.woowahaneats.domain.order.exception.OrderNotFoundException;
import com.flab.woowahaneats.domain.order.owner.domain.OwnerOrder;
import com.flab.woowahaneats.domain.order.owner.domain.OwnerOrderStatus;
import com.flab.woowahaneats.domain.order.owner.repository.OwnerOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OwnerOrderService {

    private final OwnerOrderRepository ownerOrderRepository;
    private final ApplicationEventPublisher eventPublisher;

    public void createOrder(
            UUID userOrderId,
            Long restaurantId,
            List<OrderMenu> orderMenus,
            OrderRequest orderRequest,
            OrderPrice orderPrice,
            Address deliveryAddress,
            LocalDateTime createdAt
    ) {
        OwnerOrder ownerOrder = OwnerOrder.builder()
                .id(UUID.randomUUID())
                .userOrderId(userOrderId)
                .restaurantId(restaurantId)
                .orderMenus(orderMenus)
                .orderRequest(orderRequest)
                .orderPrice(orderPrice)
                .deliveryAddress(deliveryAddress)
                .status(OwnerOrderStatus.PENDING)
                .createdAt(createdAt)
                .build();

        ownerOrderRepository.save(ownerOrder);
    }

    public void approveOrder(UUID userOrderId) {
        OwnerOrder ownerOrder = ownerOrderRepository.findByUserOrderId(userOrderId)
                .orElseThrow(OrderNotFoundException::new);

        ownerOrder.approve();
        ownerOrderRepository.save(ownerOrder);

        eventPublisher.publishEvent(new OrderAcceptedEvent(this, userOrderId));
    }

    public void startCooking(UUID userOrderId) {
        OwnerOrder ownerOrder = ownerOrderRepository.findByUserOrderId(userOrderId)
                .orElseThrow(OrderNotFoundException::new);

        ownerOrder.startCooking();
        ownerOrderRepository.save(ownerOrder);

        eventPublisher.publishEvent(new OwnerOrderCookingStartedEvent(this, userOrderId));
    }

    public void completeCooking(UUID userOrderId) {
        OwnerOrder ownerOrder = ownerOrderRepository.findByUserOrderId(userOrderId)
                .orElseThrow(OrderNotFoundException::new);

        ownerOrder.completeCooking();
        ownerOrderRepository.save(ownerOrder);

        eventPublisher.publishEvent(new OwnerOrderCookingCompletedEvent(this, userOrderId));
    }

    public void cancelOrder(UUID userOrderId) {
        OwnerOrder ownerOrder = ownerOrderRepository.findByUserOrderId(userOrderId)
                .orElseThrow(OrderNotFoundException::new);

        ownerOrder.cancel();
        ownerOrderRepository.save(ownerOrder);
    }
}
