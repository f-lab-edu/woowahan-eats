package com.flab.woowahaneats.domain.order.owner.repository;

import com.flab.woowahaneats.domain.order.owner.domain.OwnerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OwnerOrderRepository extends JpaRepository<OwnerOrder, Long> {
    @Query("SELECT o FROM OwnerOrder o WHERE o.userOrder.id = :userOrderId")
    Optional<OwnerOrder> findByUserOrderId(@Param("userOrderId") Long userOrderId);
}
