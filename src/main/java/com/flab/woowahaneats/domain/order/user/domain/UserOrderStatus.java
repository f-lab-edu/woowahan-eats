package com.flab.woowahaneats.domain.order.user.domain;

public enum UserOrderStatus {
    PENDING,      // 대기중
    ACCEPTED,     // 승인됨
    COOKING,      // 조리중
    READY,        // 조리완료 (픽업 대기) - 유저 화면에는 "조리중"으로 표시
    DELIVERING,   // 배달중
    COMPLETED,    // 완료
    CANCELLED     // 취소
}
