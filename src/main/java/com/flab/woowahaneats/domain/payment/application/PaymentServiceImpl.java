package com.flab.woowahaneats.domain.payment.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.woowahaneats.domain.auth.AuthContextHolder;
import com.flab.woowahaneats.domain.order.user.domain.UserOrder;
import com.flab.woowahaneats.domain.payment.domain.Payment;
import com.flab.woowahaneats.domain.payment.domain.PaymentProvider;
import com.flab.woowahaneats.domain.payment.event.PaymentCompletedEvent;
import com.flab.woowahaneats.domain.payment.exception.PaymentApprovalFailedException;
import com.flab.woowahaneats.domain.payment.exception.PaymentNotBelongToUserException;
import com.flab.woowahaneats.domain.payment.exception.PaymentNotFoundException;
import com.flab.woowahaneats.domain.payment.gateway.PaymentApprovalResult;
import com.flab.woowahaneats.domain.payment.gateway.PaymentGateway;
import com.flab.woowahaneats.domain.payment.gateway.PaymentGatewayResolver;
import com.flab.woowahaneats.domain.payment.outbox.OutboxEvent;
import com.flab.woowahaneats.domain.payment.outbox.OutboxEventRepository;
import com.flab.woowahaneats.domain.payment.repository.PaymentRepository;
import com.flab.woowahaneats.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentGatewayResolver gatewayRegistry;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;
    private final PaymentFailureRecorder failureRecorder;

    @Override
    @Transactional
    public Payment preparePayment(UserOrder order, int amount, PaymentProvider provider) {
        PaymentGateway gateway = gatewayRegistry.getGateway(provider);
        String gatewayOrderId = gateway.generateGatewayOrderId(order.getId());

        Payment payment = Payment.prepare(order, amount, provider, gatewayOrderId);
        paymentRepository.save(payment);
        return payment;
    }

    @Override
    @Transactional
    public void confirmPayment(String paymentKey, String gatewayOrderId, int amount) {
        User user = AuthContextHolder.getContext().getUser();
        Payment payment = paymentRepository.findByGatewayOrderId(gatewayOrderId)
                .orElseThrow(PaymentNotFoundException::new);
        UserOrder order = payment.getOrder();

        if (!order.getUser().getId().equals(user.getId())) {
            throw new PaymentNotBelongToUserException();
        }

        payment.validateAmount(amount);

        try {
            approvePayment(payment, paymentKey, gatewayOrderId, amount);
            paymentRepository.save(payment);

            PaymentCompletedEvent event = new PaymentCompletedEvent(
                    order.getId(),
                    order.getUser().getId(),
                    order.getRestaurant().getId(),
                    order.getOrderMenus(),
                    order.getOrderRequest(),
                    order.getOrderPrice(),
                    order.getDeliveryAddress(),
                    order.getCreatedAt()
            );
            outboxEventRepository.save(OutboxEvent.create("PaymentCompletedEvent", serialize(event)));
        } catch (Exception e) {
            failureRecorder.recordFailure(payment, e.getMessage());
            throw new PaymentApprovalFailedException("결제 승인 실패: " + e.getMessage());
        }
    }

    private String serialize(Object event) throws JsonProcessingException {
        return objectMapper.writeValueAsString(event);
    }

    private void approvePayment(Payment payment, String paymentKey, String gatewayOrderId, int amount) {
        PaymentGateway gateway = gatewayRegistry.getGateway(payment.getProvider());
        PaymentApprovalResult result = gateway.confirmPayment(paymentKey, gatewayOrderId, amount);
        payment.approve(result.paymentKey(), result.method());
    }

    @Override
    @Transactional
    public void refundPayment(Long orderId, String reason) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(PaymentNotFoundException::new);

        payment.refund();

        PaymentGateway gateway = gatewayRegistry.getGateway(payment.getProvider());
        gateway.cancelPayment(payment.getPaymentKey(), reason);

        paymentRepository.save(payment);
    }
}
