package com.flab.woowahaneats.domain.order.repository;

import com.flab.woowahaneats.domain.order.domain.Order;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    void save(Order order);
    Optional<Order> findById(UUID orderId);
}
