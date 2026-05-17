package com.flab.woowahaneats.domain.payment.outbox;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class OutboxEventMarkPublisher {

    private final OutboxEventRepository outboxEventRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markPublished(OutboxEvent outboxEvent) {
        outboxEvent.markPublished();
        outboxEventRepository.save(outboxEvent);
    }
}
