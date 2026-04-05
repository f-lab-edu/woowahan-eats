package com.flab.woowahaneats.domain.order.owner.application;

import com.flab.woowahaneats.domain.common.vo.Address;
import com.flab.woowahaneats.domain.order.common.OrderMenu;
import com.flab.woowahaneats.domain.order.common.OrderPrice;
import com.flab.woowahaneats.domain.order.common.OrderRequest;
import com.flab.woowahaneats.domain.order.exception.OrderNotFoundException;
import com.flab.woowahaneats.domain.order.owner.domain.OwnerOrder;
import com.flab.woowahaneats.domain.order.owner.domain.OwnerOrderStatus;
import com.flab.woowahaneats.domain.order.owner.event.OwnerOrderAcceptedEvent;
import com.flab.woowahaneats.domain.order.owner.event.OwnerOrderCookingCompletedEvent;
import com.flab.woowahaneats.domain.order.owner.event.OwnerOrderCookingStartedEvent;
import com.flab.woowahaneats.domain.order.owner.repository.OwnerOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OwnerOrderService {

    private final OwnerOrderRepository ownerOrderRepository;
    private final ApplicationEventPublisher eventPublisher;

    public void createOrder(
            Long userOrderId,
            Long restaurantId,
            List<OrderMenu> orderMenus,
            OrderRequest orderRequest,
            OrderPrice orderPrice,
            Address deliveryAddress,
            LocalDateTime createdAt
    ) {
        OwnerOrder ownerOrder = OwnerOrder.builder()
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

    public void approveOrder(Long userOrderId) {
        OwnerOrder ownerOrder = ownerOrderRepository.findByUserOrderId(userOrderId)
                .orElseThrow(OrderNotFoundException::new);

        ownerOrder.approve();
        ownerOrderRepository.save(ownerOrder);

        eventPublisher.publishEvent(new OwnerOrderAcceptedEvent(userOrderId));
    }

    public void startCooking(Long userOrderId) {
        OwnerOrder ownerOrder = ownerOrderRepository.findByUserOrderId(userOrderId)
                .orElseThrow(OrderNotFoundException::new);

        ownerOrder.startCooking();
        ownerOrderRepository.save(ownerOrder);

        eventPublisher.publishEvent(new OwnerOrderCookingStartedEvent(userOrderId));
    }

    public void completeCooking(Long userOrderId) {
        OwnerOrder ownerOrder = ownerOrderRepository.findByUserOrderId(userOrderId)
                .orElseThrow(OrderNotFoundException::new);

        ownerOrder.completeCooking();
        ownerOrderRepository.save(ownerOrder);

        eventPublisher.publishEvent(new OwnerOrderCookingCompletedEvent(userOrderId));
    }

    public void cancelOrder(Long userOrderId) {
        OwnerOrder ownerOrder = ownerOrderRepository.findByUserOrderId(userOrderId)
                .orElseThrow(OrderNotFoundException::new);

        ownerOrder.cancel();
        ownerOrderRepository.save(ownerOrder);
    }
}
