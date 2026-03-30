package com.flab.woowahaneats.domain.delivery.repository;

import com.flab.woowahaneats.domain.delivery.domain.Delivery;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryRepository {
    void save(Delivery delivery);
    Optional<Delivery> findById(UUID deliveryId);
    Optional<Delivery> findByOrderId(UUID orderId);
}