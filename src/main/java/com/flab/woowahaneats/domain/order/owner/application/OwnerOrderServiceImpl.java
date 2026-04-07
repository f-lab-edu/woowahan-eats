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
import com.flab.woowahaneats.domain.order.user.domain.UserOrder;
import com.flab.woowahaneats.domain.order.user.repository.UserOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OwnerOrderServiceImpl implements OwnerOrderService {

    private final OwnerOrderRepository ownerOrderRepository;
    private final UserOrderRepository userOrderRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public void createOrder(
            Long userOrderId,
            List<OrderMenu> orderMenus,
            OrderRequest orderRequest,
            OrderPrice orderPrice,
            Address deliveryAddress,
            LocalDateTime createdAt
    ) {
        UserOrder userOrder = userOrderRepository.findById(userOrderId)
                .orElseThrow(OrderNotFoundException::new);

        OwnerOrder ownerOrder = OwnerOrder.builder()
                .userOrder(userOrder)
                .restaurant(userOrder.getRestaurant())
                .orderMenus(orderMenus)
                .orderRequest(orderRequest)
                .orderPrice(orderPrice)
                .deliveryAddress(deliveryAddress)
                .status(OwnerOrderStatus.PENDING)
                .createdAt(createdAt)
                .build();

        ownerOrderRepository.save(ownerOrder);
    }

    @Override
    @Transactional
    public void approveOrder(Long userOrderId) {
        OwnerOrder ownerOrder = ownerOrderRepository.findByUserOrderId(userOrderId)
                .orElseThrow(OrderNotFoundException::new);

        ownerOrder.approve();
        ownerOrderRepository.save(ownerOrder);

        eventPublisher.publishEvent(new OwnerOrderAcceptedEvent(userOrderId));
    }

    @Override
    @Transactional
    public void startCooking(Long userOrderId) {
        OwnerOrder ownerOrder = ownerOrderRepository.findByUserOrderId(userOrderId)
                .orElseThrow(OrderNotFoundException::new);

        ownerOrder.startCooking();
        ownerOrderRepository.save(ownerOrder);

        eventPublisher.publishEvent(new OwnerOrderCookingStartedEvent(userOrderId));
    }

    @Override
    @Transactional
    public void completeCooking(Long userOrderId) {
        OwnerOrder ownerOrder = ownerOrderRepository.findByUserOrderId(userOrderId)
                .orElseThrow(OrderNotFoundException::new);

        ownerOrder.completeCooking();
        ownerOrderRepository.save(ownerOrder);

        eventPublisher.publishEvent(new OwnerOrderCookingCompletedEvent(userOrderId));
    }

    @Override
    @Transactional
    public void cancelOrder(Long userOrderId) {
        OwnerOrder ownerOrder = ownerOrderRepository.findByUserOrderId(userOrderId)
                .orElseThrow(OrderNotFoundException::new);

        ownerOrder.cancel();
        ownerOrderRepository.save(ownerOrder);
    }
}
