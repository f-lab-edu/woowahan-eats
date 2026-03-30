package com.flab.woowahaneats.domain.payment.repository;

import com.flab.woowahaneats.domain.payment.domain.Payment;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Repository
public class HashMapPaymentRepository implements PaymentRepository {

    private final HashMap<UUID, Payment> paymentMap = new HashMap<>();

    @Override
    public void save(Payment payment) {
        paymentMap.put(payment.getId(), payment);
    }

    @Override
    public Optional<Payment> findByOrderId(UUID orderId) {
        return paymentMap.values().stream()
                .filter(payment -> payment.getOrderId().equals(orderId))
                .findFirst();
    }

    @Override
    public Optional<Payment> findByTossOrderId(String tossOrderId) {
        return paymentMap.values().stream()
                .filter(payment -> payment.getTossOrderId().equals(tossOrderId))
                .findFirst();
    }
}