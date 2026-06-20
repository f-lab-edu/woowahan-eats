package com.flab.woowahaneats.domain.delivery.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.woowahaneats.domain.delivery.controller.dto.DeliveryResponse;
import com.flab.woowahaneats.domain.delivery.domain.Delivery;
import com.flab.woowahaneats.domain.delivery.domain.DeliveryStatus;
import com.flab.woowahaneats.domain.delivery.event.DeliveryAcceptedEvent;
import com.flab.woowahaneats.domain.delivery.event.DeliveryCancelledEvent;
import com.flab.woowahaneats.domain.delivery.event.DeliveryCompletedEvent;
import com.flab.woowahaneats.domain.delivery.event.DeliveryCreatedEvent;
import com.flab.woowahaneats.domain.delivery.exception.DeliveryNotFoundException;
import com.flab.woowahaneats.domain.delivery.exception.OrderNotReadyForDeliveryException;
import com.flab.woowahaneats.domain.delivery.repository.DeliveryRepository;
import com.flab.woowahaneats.domain.order.exception.OrderNotFoundException;
import com.flab.woowahaneats.domain.order.owner.domain.OwnerOrder;
import com.flab.woowahaneats.domain.order.owner.domain.OwnerOrderStatus;
import com.flab.woowahaneats.domain.order.owner.repository.OwnerOrderRepository;
import com.flab.woowahaneats.domain.order.user.domain.UserOrder;
import com.flab.woowahaneats.domain.order.user.repository.UserOrderRepository;
import com.flab.woowahaneats.domain.payment.outbox.OutboxEvent;
import com.flab.woowahaneats.domain.payment.outbox.OutboxEventRepository;
import com.flab.woowahaneats.domain.rider.domain.Rider;
import com.flab.woowahaneats.domain.rider.exception.RiderNotFoundException;
import com.flab.woowahaneats.domain.rider.repository.RiderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final OwnerOrderRepository ownerOrderRepository;
    private final UserOrderRepository userOrderRepository;
    private final RiderRepository riderRepository;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public void createDelivery(Long userOrderId) {
        OwnerOrder ownerOrder = ownerOrderRepository.findByUserOrderId(userOrderId)
                .orElseThrow(OrderNotFoundException::new);

        if (ownerOrder.getStatus() != OwnerOrderStatus.READY) {
            throw new OrderNotReadyForDeliveryException();
        }

        UserOrder userOrder = userOrderRepository.findById(userOrderId)
                .orElseThrow(OrderNotFoundException::new);

        Delivery delivery = Delivery.create(userOrder);
        deliveryRepository.save(delivery);

        outboxEventRepository.save(OutboxEvent.create("DeliveryCreatedEvent", serialize(new DeliveryCreatedEvent(delivery.getId(), userOrderId))));
    }

    @Override
    @Transactional
    public void cancelDelivery(Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(DeliveryNotFoundException::new);

        Long riderId = delivery.getRider() != null ? delivery.getRider().getId() : null;
        delivery.cancel();
        deliveryRepository.save(delivery);

        outboxEventRepository.save(OutboxEvent.create("DeliveryCancelledEvent", serialize(new DeliveryCancelledEvent(delivery.getId(), delivery.getOrder().getId(), riderId))));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryResponse> getPendingDeliveries() {
        List<Delivery> deliveries = deliveryRepository.findByStatus(DeliveryStatus.PENDING);

        return deliveries.stream()
                .map(DeliveryResponse::from)
                .toList();
    }

    @Override
    @Transactional
    public void acceptDelivery(Long deliveryId, Long riderId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(DeliveryNotFoundException::new);

        Rider rider = riderRepository.findById(riderId)
                .orElseThrow(RiderNotFoundException::new);

        delivery.accept(rider);
        deliveryRepository.save(delivery);

        outboxEventRepository.save(OutboxEvent.create("DeliveryAcceptedEvent", serialize(new DeliveryAcceptedEvent(delivery.getId(), delivery.getOrder().getId(), riderId))));
    }

    @Override
    @Transactional
    public void startPickup(Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(DeliveryNotFoundException::new);

        delivery.startPickup();
        deliveryRepository.save(delivery);
    }

    @Override
    @Transactional
    public void completePickup(Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(DeliveryNotFoundException::new);

        delivery.completePickup();
        deliveryRepository.save(delivery);
    }

    @Override
    @Transactional
    public void startDelivery(Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(DeliveryNotFoundException::new);

        delivery.startDelivery();
        deliveryRepository.save(delivery);
    }

    @Override
    @Transactional
    public void completeDelivery(Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(DeliveryNotFoundException::new);

        Long riderId = delivery.getRider() != null ? delivery.getRider().getId() : null;
        delivery.complete();
        deliveryRepository.save(delivery);

        outboxEventRepository.save(OutboxEvent.create("DeliveryCompletedEvent", serialize(new DeliveryCompletedEvent(delivery.getId(), delivery.getOrder().getId(), riderId))));
    }

    private String serialize(Object event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (Exception e) {
            throw new RuntimeException("이벤트 직렬화 실패: " + event.getClass().getSimpleName(), e);
        }
    }
}
