package com.flab.woowahaneats.domain.payment.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.woowahaneats.domain.delivery.event.DeliveryAcceptedEvent;
import com.flab.woowahaneats.domain.delivery.event.DeliveryCancelledEvent;
import com.flab.woowahaneats.domain.delivery.event.DeliveryCompletedEvent;
import com.flab.woowahaneats.domain.delivery.event.DeliveryCreatedEvent;
import com.flab.woowahaneats.domain.order.owner.application.OwnerOrderService;
import com.flab.woowahaneats.domain.order.owner.event.OwnerOrderAcceptedEvent;
import com.flab.woowahaneats.domain.order.owner.event.OwnerOrderCookingCompletedEvent;
import com.flab.woowahaneats.domain.order.owner.event.OwnerOrderCookingStartedEvent;
import com.flab.woowahaneats.domain.order.user.application.UserOrderService;
import com.flab.woowahaneats.domain.order.user.event.UserOrderCancelledEvent;
import com.flab.woowahaneats.domain.payment.event.PaymentCompletedEvent;
import com.flab.woowahaneats.domain.rider.application.RiderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxEventPoller {

    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;
    private final OwnerOrderService ownerOrderService;
    private final UserOrderService userOrderService;
    private final RiderService riderService;
    private final OutboxEventMarkPublisher markPublisher;

    @Scheduled(fixedDelay = 5000)
    public void poll() {
        List<OutboxEvent> unpublished = outboxEventRepository.findByPublishedFalse();
        for (OutboxEvent outboxEvent : unpublished) {
            try {
                dispatch(outboxEvent);
                markPublisher.markPublished(outboxEvent);
            } catch (Exception e) {
                log.error("Outbox 이벤트 처리 실패 [id={}, type={}]: {}",
                        outboxEvent.getId(), outboxEvent.getEventType(), e.getMessage(), e);
            }
        }
    }

    private void dispatch(OutboxEvent outboxEvent) throws Exception {
        String type = outboxEvent.getEventType();
        String payload = outboxEvent.getPayload();

        switch (type) {
            case "PaymentCompletedEvent" -> {
                PaymentCompletedEvent event = objectMapper.readValue(payload, PaymentCompletedEvent.class);
                ownerOrderService.createOrder(
                        event.userOrderId(),
                        event.orderMenus(),
                        event.orderRequest(),
                        event.orderPrice(),
                        event.deliveryAddress(),
                        event.createdAt()
                );
            }
            case "UserOrderCancelledEvent" -> {
                UserOrderCancelledEvent event = objectMapper.readValue(payload, UserOrderCancelledEvent.class);
                ownerOrderService.cancelOrder(event.userOrderId());
            }
            case "OwnerOrderAcceptedEvent" -> {
                OwnerOrderAcceptedEvent event = objectMapper.readValue(payload, OwnerOrderAcceptedEvent.class);
                userOrderService.approveOrder(event.userOrderId());
            }
            case "OwnerOrderCookingStartedEvent" -> {
                OwnerOrderCookingStartedEvent event = objectMapper.readValue(payload, OwnerOrderCookingStartedEvent.class);
                userOrderService.startCooking(event.userOrderId());
            }
            case "OwnerOrderCookingCompletedEvent" -> {
                OwnerOrderCookingCompletedEvent event = objectMapper.readValue(payload, OwnerOrderCookingCompletedEvent.class);
                userOrderService.completeCooking(event.userOrderId());
            }
            case "DeliveryCreatedEvent" -> {
                DeliveryCreatedEvent event = objectMapper.readValue(payload, DeliveryCreatedEvent.class);
                userOrderService.startDelivering(event.orderId());
            }
            case "DeliveryCancelledEvent" -> {
                DeliveryCancelledEvent event = objectMapper.readValue(payload, DeliveryCancelledEvent.class);
                userOrderService.resetOrderToReady(event.orderId());
                if (event.riderId() != null) {
                    riderService.finishDelivering(event.riderId());
                }
            }
            case "DeliveryAcceptedEvent" -> {
                DeliveryAcceptedEvent event = objectMapper.readValue(payload, DeliveryAcceptedEvent.class);
                riderService.startDelivering(event.riderId());
            }
            case "DeliveryCompletedEvent" -> {
                DeliveryCompletedEvent event = objectMapper.readValue(payload, DeliveryCompletedEvent.class);
                userOrderService.completeOrder(event.orderId());
                if (event.riderId() != null) {
                    riderService.finishDelivering(event.riderId());
                }
            }
            default -> throw new IllegalArgumentException("알 수 없는 이벤트 타입: " + type);
        }
    }
}
