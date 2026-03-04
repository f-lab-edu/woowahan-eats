package com.flab.woowahaneats.domain.menu.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Menu {
    private Long id;
    private Long restaurantId;
    private String name;
    private String description;
    private String ImageUrl;
    private int price;
    private boolean isAvailable;
}
