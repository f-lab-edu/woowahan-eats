package com.flab.woowahaneats.domain.payment.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.woowahaneats.domain.order.owner.application.OwnerOrderService;
import com.flab.woowahaneats.domain.payment.event.PaymentCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxEventPoller {

    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;
    private final OwnerOrderService ownerOrderService;

    @Scheduled(fixedDelay = 5000)
    public void poll() {
        List<OutboxEvent> unpublished = outboxEventRepository.findByPublishedFalse();
        for (OutboxEvent outboxEvent : unpublished) {
            try {
                dispatch(outboxEvent);
                markPublished(outboxEvent);
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
            default -> throw new IllegalArgumentException("알 수 없는 이벤트 타입: " + type);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markPublished(OutboxEvent outboxEvent) {
        outboxEvent.markPublished();
        outboxEventRepository.save(outboxEvent);
    }
}
