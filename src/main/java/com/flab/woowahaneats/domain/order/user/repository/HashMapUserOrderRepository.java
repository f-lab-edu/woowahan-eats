package com.flab.woowahaneats.domain.order.user.repository;

import com.flab.woowahaneats.domain.order.user.domain.UserOrder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class HashMapUserOrderRepository implements UserOrderRepository {

    HashMap<UUID, UserOrder> orderMap = new HashMap<>();

    @Override
    public void save(UserOrder order) {
        orderMap.put(order.getId(), order);
    }

    @Override
    public Optional<UserOrder> findById(UUID orderId) {
        return Optional.ofNullable(orderMap.get(orderId));
    }

    @Override
    public List<UserOrder> findActiveOrdersByUserId(Long userId) {
        return orderMap.values().stream()
                .filter(order -> order.getUserId().equals(userId))
                .filter(UserOrder::isActive)
                .toList();
    }
}
