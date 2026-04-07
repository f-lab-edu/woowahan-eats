package com.flab.woowahaneats.domain.order.user.repository;

import com.flab.woowahaneats.domain.order.user.domain.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserOrderRepository extends JpaRepository<UserOrder, Long> {
    @Query("SELECT o FROM UserOrder o WHERE o.user.id = :userId AND (o.status != 'CANCELLED' AND o.status != 'COMPLETED')")
    List<UserOrder> findActiveOrdersByUserId(@Param("userId") Long userId);
}
