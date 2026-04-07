package com.flab.woowahaneats.domain.restaurant.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "restaurant_operation_infos")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
public class RestaurantOperationInfo {
    @Id
    @Column(name = "restaurant_id")
    private Long restaurantId;

    @Column(name = "min_order_amt", nullable = false)
    private int minOrderAmt;

    @Column(name = "delivery_fee", nullable = false)
    private int deliveryFee;

    @Column(nullable = false)
    private boolean open;

    public static RestaurantOperationInfo create(Long restaurantId, int minOrderAmt, int deliveryFee) {
        return RestaurantOperationInfo.builder()
                .restaurantId(restaurantId)
                .minOrderAmt(minOrderAmt)
                .deliveryFee(deliveryFee)
                .open(false)
                .build();
    }
}
