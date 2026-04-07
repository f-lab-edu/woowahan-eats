package com.flab.woowahaneats.domain.delivery.application;

import com.flab.woowahaneats.domain.delivery.controller.dto.DeliveryResponse;

import java.util.List;

public interface DeliveryService {

    void createDelivery(Long userOrderId);

    void cancelDelivery(Long deliveryId);

    List<DeliveryResponse> getPendingDeliveries();

    void acceptDelivery(Long deliveryId, Long riderId);

    void startPickup(Long deliveryId);

    void completePickup(Long deliveryId);

    void startDelivery(Long deliveryId);

    void completeDelivery(Long deliveryId);
}
