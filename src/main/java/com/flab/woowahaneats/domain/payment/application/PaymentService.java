package com.flab.woowahaneats.domain.payment.application;

import com.flab.woowahaneats.domain.order.event.PaymentCompletedEvent;
import com.flab.woowahaneats.domain.payment.domain.Payment;
import com.flab.woowahaneats.domain.payment.domain.PaymentStatus;
import com.flab.woowahaneats.domain.payment.exception.PaymentApprovalFailedException;
import com.flab.woowahaneats.domain.payment.exception.PaymentNotFoundException;
import com.flab.woowahaneats.domain.payment.gateway.PaymentApprovalResult;
import com.flab.woowahaneats.domain.payment.gateway.PaymentGateway;
import com.flab.woowahaneats.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentGateway paymentGateway;
    private final ApplicationEventPublisher eventPublisher;

    public Payment preparePayment(UUID orderId, int amount) {
        Payment payment = Payment.prepare(orderId, amount);
        paymentRepository.save(payment);
        return payment;
    }

    public void confirmPayment(String paymentKey, String tossOrderId, int amount) {
        Payment payment = paymentRepository.findByTossOrderId(tossOrderId)
                .orElseThrow(PaymentNotFoundException::new);

        payment.validateAmount(amount);

        approvePayment(payment, paymentKey, tossOrderId, amount);

        paymentRepository.save(payment);
        eventPublisher.publishEvent(new PaymentCompletedEvent(this, payment.getOrderId()));
    }

    private void approvePayment(Payment payment, String paymentKey, String tossOrderId, int amount) {
        try {
            PaymentApprovalResult result = paymentGateway.confirmPayment(
                    paymentKey, tossOrderId, amount
            );
            payment.approve(result.paymentKey(), result.method());
        } catch (Exception e) {
            payment.fail(e.getMessage());
            paymentRepository.save(payment);
            throw new PaymentApprovalFailedException("결제 승인 실패: " + e.getMessage());
        }
    }

    public void refundPayment(UUID orderId, String reason) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(PaymentNotFoundException::new);

        if (payment.getStatus() != PaymentStatus.COMPLETED) {
            return;
        }

        paymentGateway.cancelPayment(payment.getPaymentKey(), reason);

        payment.refund();
        paymentRepository.save(payment);
    }
}