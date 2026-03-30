package com.flab.woowahaneats.domain.member.event;

import com.flab.woowahaneats.domain.delivery.event.DeliveryAcceptedEvent;
import com.flab.woowahaneats.domain.delivery.event.DeliveryCancelledEvent;
import com.flab.woowahaneats.domain.delivery.event.DeliveryCompletedEvent;
import com.flab.woowahaneats.domain.member.application.RiderService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RiderEventHandler {

    private final RiderService riderService;

    @EventListener
    public void handleDeliveryAccepted(DeliveryAcceptedEvent event) {
        riderService.startDelivering(event.getRiderId());
    }

    @EventListener
    public void handleDeliveryCompleted(DeliveryCompletedEvent event) {
        if (event.getRiderId() != null) {
            riderService.finishDelivering(event.getRiderId());
        }
    }

    @EventListener
    public void handleDeliveryCancelled(DeliveryCancelledEvent event) {
        if (event.getRiderId() != null) {
            riderService.finishDelivering(event.getRiderId());
        }
    }
}