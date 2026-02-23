package com.flab.woowahaneats.domain.restaurant.domain;

import com.flab.woowahaneats.domain.common.vo.Address;
import com.flab.woowahaneats.domain.common.vo.Location;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Restaurant {
    private Long id;
    private Long memberId;
    private String name;
    private String description;
    private Address address;
    private Location location;
    private String region;
    private double avgRating;
}
