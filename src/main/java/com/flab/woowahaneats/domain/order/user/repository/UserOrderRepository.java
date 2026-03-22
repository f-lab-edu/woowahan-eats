package com.flab.woowahaneats.domain.order.user.repository;

import com.flab.woowahaneats.domain.order.user.domain.UserOrder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserOrderRepository {
    void save(UserOrder order);
    Optional<UserOrder> findById(UUID orderId);
    List<UserOrder> findActiveOrdersByUserId(Long userId);
}
