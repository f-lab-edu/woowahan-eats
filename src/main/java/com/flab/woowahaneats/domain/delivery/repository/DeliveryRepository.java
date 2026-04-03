package com.flab.woowahaneats.domain.delivery.repository;

import com.flab.woowahaneats.domain.delivery.domain.Delivery;
import com.flab.woowahaneats.domain.delivery.domain.DeliveryStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryRepository {
    void save(Delivery delivery);
    Optional<Delivery> findById(UUID deliveryId);
    List<Delivery> findByStatus(DeliveryStatus status);
}