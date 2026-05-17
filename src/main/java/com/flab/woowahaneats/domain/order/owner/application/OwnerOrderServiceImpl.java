package com.flab.woowahaneats.domain.order.owner.application;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.flab.woowahaneats.domain.payment.outbox.OutboxEvent;
import com.flab.woowahaneats.domain.payment.outbox.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OwnerOrderServiceImpl implements OwnerOrderService {

    private final OwnerOrderRepository ownerOrderRepository;
    private final UserOrderRepository userOrderRepository;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

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

        outboxEventRepository.save(OutboxEvent.create("OwnerOrderAcceptedEvent", serialize(new OwnerOrderAcceptedEvent(userOrderId))));
    }

    @Override
    @Transactional
    public void startCooking(Long userOrderId) {
        OwnerOrder ownerOrder = ownerOrderRepository.findByUserOrderId(userOrderId)
                .orElseThrow(OrderNotFoundException::new);

        ownerOrder.startCooking();
        ownerOrderRepository.save(ownerOrder);

        outboxEventRepository.save(OutboxEvent.create("OwnerOrderCookingStartedEvent", serialize(new OwnerOrderCookingStartedEvent(userOrderId))));
    }

    @Override
    @Transactional
    public void completeCooking(Long userOrderId) {
        OwnerOrder ownerOrder = ownerOrderRepository.findByUserOrderId(userOrderId)
                .orElseThrow(OrderNotFoundException::new);

        ownerOrder.completeCooking();
        ownerOrderRepository.save(ownerOrder);

        outboxEventRepository.save(OutboxEvent.create("OwnerOrderCookingCompletedEvent", serialize(new OwnerOrderCookingCompletedEvent(userOrderId))));
    }

    private String serialize(Object event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (Exception e) {
            throw new RuntimeException("이벤트 직렬화 실패: " + event.getClass().getSimpleName(), e);
        }
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
