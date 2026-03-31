package com.flab.woowahaneats.domain.payment.application;

import com.flab.woowahaneats.domain.auth.AuthContextHolder;
import com.flab.woowahaneats.domain.member.domain.User;
import com.flab.woowahaneats.domain.order.event.PaymentCompletedEvent;
import com.flab.woowahaneats.domain.order.user.domain.UserOrder;
import com.flab.woowahaneats.domain.order.user.repository.UserOrderRepository;
import com.flab.woowahaneats.domain.payment.domain.Payment;
import com.flab.woowahaneats.domain.payment.exception.PaymentApprovalFailedException;
import com.flab.woowahaneats.domain.payment.exception.PaymentNotBelongToUserException;
import com.flab.woowahaneats.domain.payment.exception.PaymentNotFoundException;
import com.flab.woowahaneats.domain.payment.exception.PaymentOrderNotFoundException;
import com.flab.woowahaneats.domain.payment.gateway.PaymentApprovalResult;
import com.flab.woowahaneats.domain.payment.gateway.PaymentGateway;
import com.flab.woowahaneats.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserOrderRepository userOrderRepository;
    private final PaymentGateway paymentGateway;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Payment preparePayment(UUID orderId, int amount) {
        Payment payment = Payment.prepare(orderId, amount);
        paymentRepository.save(payment);
        return payment;
    }

    @Transactional
    public void confirmPayment(String paymentKey, String tossOrderId, int amount) {
        User user = AuthContextHolder.getContext().getUser();
        Payment payment = paymentRepository.findByTossOrderId(tossOrderId)
                .orElseThrow(PaymentNotFoundException::new);
        UserOrder order = userOrderRepository.findById(payment.getOrderId())
                .orElseThrow(PaymentOrderNotFoundException::new);

        if (!order.getUserId().equals(user.getId())) {
            throw new PaymentNotBelongToUserException();
        }

        payment.validateAmount(amount);

        approvePayment(payment, paymentKey, tossOrderId, amount);

        paymentRepository.save(payment);

        eventPublisher.publishEvent(new PaymentCompletedEvent(
                this,
                order.getId(),
                order.getUserId(),
                order.getRestaurantId(),
                order.getOrderMenus(),
                order.getOrderRequest(),
                order.getOrderPrice(),
                order.getDeliveryAddress(),
                order.getCreatedAt()
        ));
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

    @Transactional
    public void refundPayment(UUID orderId, String reason) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(PaymentNotFoundException::new);

        payment.refund();

        paymentGateway.cancelPayment(payment.getPaymentKey(), reason);
        paymentRepository.save(payment);
    }
}
