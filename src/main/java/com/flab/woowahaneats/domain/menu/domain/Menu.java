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

    public Menu update(String internalName, String displayName, String description,
                       String imageUrl, Integer price, Boolean available) {
        return this.toBuilder()
                .internalName(internalName != null ? internalName : this.internalName)
                .displayName(displayName != null ? displayName : this.displayName)
                .description(description != null ? description : this.description)
                .imageUrl(imageUrl != null ? imageUrl : this.imageUrl)
                .price(price != null ? price : this.price)
                .available(available != null ? available : this.available)
                .build();
    }
}
