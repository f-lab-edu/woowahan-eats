package com.flab.woowahaneats.domain.payment.application;

import com.flab.woowahaneats.domain.auth.AuthContextHolder;
import com.flab.woowahaneats.domain.member.domain.User;
import com.flab.woowahaneats.domain.order.event.PaymentCompletedEvent;
import com.flab.woowahaneats.domain.order.user.domain.UserOrder;
import com.flab.woowahaneats.domain.order.user.repository.UserOrderRepository;
import com.flab.woowahaneats.domain.payment.domain.Payment;
import com.flab.woowahaneats.domain.payment.domain.PaymentProvider;
import com.flab.woowahaneats.domain.payment.exception.PaymentApprovalFailedException;
import com.flab.woowahaneats.domain.payment.exception.PaymentNotBelongToUserException;
import com.flab.woowahaneats.domain.payment.exception.PaymentNotFoundException;
import com.flab.woowahaneats.domain.payment.exception.PaymentOrderNotFoundException;
import com.flab.woowahaneats.domain.payment.gateway.PaymentApprovalResult;
import com.flab.woowahaneats.domain.payment.gateway.PaymentGateway;
import com.flab.woowahaneats.domain.payment.gateway.PaymentGatewayResolver;
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
    private final PaymentGatewayResolver gatewayRegistry;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Payment preparePayment(UUID orderId, int amount, PaymentProvider provider) {
        PaymentGateway gateway = gatewayRegistry.getGateway(provider);
        String gatewayOrderId = gateway.generateGatewayOrderId(orderId);

        Payment payment = Payment.prepare(orderId, amount, provider, gatewayOrderId);
        paymentRepository.save(payment);
        return payment;
    }

    @Transactional
    public void confirmPayment(String paymentKey, String gatewayOrderId, int amount) {
        User user = AuthContextHolder.getContext().getUser();
        Payment payment = paymentRepository.findByGatewayOrderId(gatewayOrderId)
                .orElseThrow(PaymentNotFoundException::new);
        UserOrder order = userOrderRepository.findById(payment.getOrderId())
                .orElseThrow(PaymentOrderNotFoundException::new);

        if (!order.getUserId().equals(user.getId())) {
            throw new PaymentNotBelongToUserException();
        }

        payment.validateAmount(amount);

        try {
            approvePayment(payment, paymentKey, gatewayOrderId, amount);
            paymentRepository.save(payment);

            eventPublisher.publishEvent(new PaymentCompletedEvent(
                    order.getId(),
                    order.getUserId(),
                    order.getRestaurantId(),
                    order.getOrderMenus(),
                    order.getOrderRequest(),
                    order.getOrderPrice(),
                    order.getDeliveryAddress(),
                    order.getCreatedAt()
            ));
        } catch (Exception e) {
            payment.fail(e.getMessage());
            paymentRepository.save(payment);
            throw new PaymentApprovalFailedException("결제 승인 실패: " + e.getMessage());
        }
    }

    private void approvePayment(Payment payment, String paymentKey, String gatewayOrderId, int amount) {
        PaymentGateway gateway = gatewayRegistry.getGateway(payment.getProvider());
        PaymentApprovalResult result = gateway.confirmPayment(
                paymentKey, gatewayOrderId, amount
        );
        payment.approve(result.paymentKey(), result.method());
    }

    @Transactional
    public void refundPayment(UUID orderId, String reason) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(PaymentNotFoundException::new);

        payment.refund();

        PaymentGateway gateway = gatewayRegistry.getGateway(payment.getProvider());
        gateway.cancelPayment(payment.getPaymentKey(), reason);

        paymentRepository.save(payment);
    }
}
