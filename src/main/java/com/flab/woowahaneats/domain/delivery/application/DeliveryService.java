package com.flab.woowahaneats.domain.delivery.application;

import com.flab.woowahaneats.domain.delivery.domain.Delivery;
import com.flab.woowahaneats.domain.delivery.event.DeliveryCancelledEvent;
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

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final OwnerOrderRepository ownerOrderRepository;
    private final ApplicationEventPublisher eventPublisher;

    public void createDelivery(UUID userOrderId) {
        OwnerOrder ownerOrder = ownerOrderRepository.findByUserOrderId(userOrderId)
                .orElseThrow(OrderNotFoundException::new);

        if (ownerOrder.getStatus() != OwnerOrderStatus.READY) {
            throw new OrderNotReadyForDeliveryException();
        }

        Delivery delivery = Delivery.create(userOrderId);
        deliveryRepository.save(delivery);

        eventPublisher.publishEvent(new DeliveryCreatedEvent(this, delivery.getId(), userOrderId));
    }

    public void cancelDelivery(UUID deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(DeliveryNotFoundException::new);

        delivery.cancel();
        deliveryRepository.save(delivery);

        eventPublisher.publishEvent(new DeliveryCancelledEvent(this, delivery.getId(), delivery.getOrderId()));
    }
}