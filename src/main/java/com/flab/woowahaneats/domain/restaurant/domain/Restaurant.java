package com.flab.woowahaneats.domain.restaurant.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Restaurant {
    private Long id;
    private Long memberId;
    private String name;
    private String description;
    private String address;
    private int minOrderAmt;
    private int deliveryFee;
    private String region;
    private boolean open;
    private double avgRating;
}
