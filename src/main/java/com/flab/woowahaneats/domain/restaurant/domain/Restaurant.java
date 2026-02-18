package com.flab.woowahaneats.domain.restaurant.domain;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class Restaurant {
    private Long id;
    private Long memberId;
    private String name;
    private String description;
    private String address;
    private int minOrderAmt;
    private BigDecimal deliveryFee;
    private String region;
    private boolean open;
    private BigDecimal avgRating;
}
