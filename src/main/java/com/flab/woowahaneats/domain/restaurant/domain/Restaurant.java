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
    private String region;
    private double avgRating;
}
