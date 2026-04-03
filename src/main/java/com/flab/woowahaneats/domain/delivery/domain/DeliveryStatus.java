package com.flab.woowahaneats.domain.delivery.domain;

public enum DeliveryStatus {
    PENDING,        // 라이더 배정 대기 중
    ASSIGNED,       // 라이더 배정 완료
    PICKING_UP,     // 라이더가 음식점으로 이동 중
    PICKED_UP,      // 음식 픽업 완료
    IN_DELIVERY,    // 고객에게 배달 중
    DELIVERED,      // 배달 완료
    CANCELLED       // 배달 취소됨

}
