package com.flab.woowahaneats.domain.order.repository;

import com.flab.woowahaneats.domain.order.domain.Order;

public interface OrderRepository {
    void save(Order order);
}
