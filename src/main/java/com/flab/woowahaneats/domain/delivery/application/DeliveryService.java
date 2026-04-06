package com.flab.woowahaneats.domain.delivery.application;

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
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final OwnerOrderRepository ownerOrderRepository;
    private final ApplicationEventPublisher eventPublisher;

    public void createDelivery(Long userOrderId) {
        OwnerOrder ownerOrder = ownerOrderRepository.findByUserOrderId(userOrderId)
                .orElseThrow(OrderNotFoundException::new);

        if (ownerOrder.getStatus() != OwnerOrderStatus.READY) {
            throw new OrderNotReadyForDeliveryException();
        }

        Delivery delivery = Delivery.create(userOrderId);
        deliveryRepository.save(delivery);

        eventPublisher.publishEvent(new DeliveryCreatedEvent(this, delivery.getId(), userOrderId));
    }

    public void cancelDelivery(Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(DeliveryNotFoundException::new);

        Long riderId = delivery.getRiderId();
        delivery.cancel();
        deliveryRepository.save(delivery);

        eventPublisher.publishEvent(new DeliveryCancelledEvent(this, delivery.getId(), delivery.getOrderId(), riderId));
    }

    public List<DeliveryResponse> getPendingDeliveries() {
        List<Delivery> deliveries = deliveryRepository.findByStatus(DeliveryStatus.PENDING);

        return deliveries.stream()
                .map(DeliveryResponse::from)
                .toList();
    }

    public void acceptDelivery(Long deliveryId, Long riderId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(DeliveryNotFoundException::new);

        delivery.accept(riderId);
        deliveryRepository.save(delivery);

        eventPublisher.publishEvent(new DeliveryAcceptedEvent(this, delivery.getId(), delivery.getOrderId(), riderId));
    }

    public void startPickup(Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(DeliveryNotFoundException::new);

        delivery.startPickup();
        deliveryRepository.save(delivery);
    }

    public void completePickup(Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(DeliveryNotFoundException::new);

        delivery.completePickup();
        deliveryRepository.save(delivery);
    }

    public void startDelivery(Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(DeliveryNotFoundException::new);

        delivery.startDelivery();
        deliveryRepository.save(delivery);
    }

    public void completeDelivery(Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(DeliveryNotFoundException::new);

        Long riderId = delivery.getRiderId();
        delivery.complete();
        deliveryRepository.save(delivery);

        eventPublisher.publishEvent(new DeliveryCompletedEvent(this, delivery.getId(), delivery.getOrderId(), riderId));
    }
}