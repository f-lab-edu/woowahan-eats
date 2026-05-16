package com.flab.woowahaneats.domain.payment.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.woowahaneats.domain.payment.event.PaymentCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxEventPoller {

    private final OutboxEventRepository outboxEventRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void poll() {
        List<OutboxEvent> unpublished = outboxEventRepository.findByPublishedFalse();
        for (OutboxEvent outboxEvent : unpublished) {
            try {
                Object event = deserialize(outboxEvent);
                eventPublisher.publishEvent(event);
                outboxEvent.markPublished();
            } catch (Exception e) {
                log.error("Outbox 이벤트 발행 실패 [id={}, type={}]: {}",
                        outboxEvent.getId(), outboxEvent.getEventType(), e.getMessage(), e);
            }
        }
    }

    private Object deserialize(OutboxEvent outboxEvent) throws Exception {
        String type = outboxEvent.getEventType();
        String payload = outboxEvent.getPayload();

        return switch (type) {
            case "PaymentCompletedEvent" -> objectMapper.readValue(payload, PaymentCompletedEvent.class);
            default -> throw new IllegalArgumentException("알 수 없는 이벤트 타입: " + type);
        };
    }
}
