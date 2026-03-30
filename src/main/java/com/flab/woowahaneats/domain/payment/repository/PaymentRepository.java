package com.flab.woowahaneats.domain.payment.repository;

import com.flab.woowahaneats.domain.payment.domain.Payment;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository {
    void save(Payment payment);
    Optional<Payment> findByOrderId(UUID orderId);
    Optional<Payment> findByTossOrderId(String tossOrderId);
}