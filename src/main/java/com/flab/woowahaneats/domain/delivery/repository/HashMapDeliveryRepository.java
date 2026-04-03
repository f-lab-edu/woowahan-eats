package com.flab.woowahaneats.domain.delivery.repository;

import com.flab.woowahaneats.domain.delivery.domain.Delivery;
import com.flab.woowahaneats.domain.delivery.domain.DeliveryStatus;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class HashMapDeliveryRepository implements DeliveryRepository {

    HashMap<UUID, Delivery> deliveryMap = new HashMap<>();

    @Override
    public void save(Delivery delivery) {
        deliveryMap.put(delivery.getId(), delivery);
    }

    @Override
    public Optional<Delivery> findById(UUID deliveryId) {
        return Optional.ofNullable(deliveryMap.get(deliveryId));
    }

    @Override
    public List<Delivery> findByStatus(DeliveryStatus status) {
        return deliveryMap.values().stream()
                .filter(delivery -> delivery.getStatus() == status)
                .toList();
    }
}