package com.flab.woowahaneats.domain.payment.repository;

import com.flab.woowahaneats.domain.payment.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrderId(UUID orderId);
    Optional<Payment> findByGatewayOrderId(String gatewayOrderId);
}