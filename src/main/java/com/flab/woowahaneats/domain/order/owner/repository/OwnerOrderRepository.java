package com.flab.woowahaneats.domain.order.owner.repository;

import com.flab.woowahaneats.domain.order.owner.domain.OwnerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OwnerOrderRepository extends JpaRepository<OwnerOrder, Long> {
    Optional<OwnerOrder> findByUserOrderId(Long userOrderId);
}
