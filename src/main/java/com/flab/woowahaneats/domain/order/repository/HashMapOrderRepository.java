package com.flab.woowahaneats.domain.order.repository;

import com.flab.woowahaneats.domain.order.domain.Order;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.UUID;

@Repository
public class HashMapOrderRepository implements OrderRepository {

    HashMap<UUID, Order> orderMap = new HashMap<>();

    @Override
    public void save(Order order) {
        orderMap.put(order.getId(), order);
    }
}
