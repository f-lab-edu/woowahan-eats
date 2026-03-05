package com.flab.woowahaneats.domain.menu.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class Menu {
    private Long id;
    private Long restaurantId;
    private String internalName;
    private String displayName;
    private String description;
    private String imageUrl;
    private int price;
    private boolean available;
}
