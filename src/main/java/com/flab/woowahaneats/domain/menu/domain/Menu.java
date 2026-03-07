package com.flab.woowahaneats.domain.menu.domain;

import com.flab.woowahaneats.domain.menu.application.exception.InvalidMenuException;
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

    public static Menu create(Long id, Long restaurantId, String internalName, String displayName,
                              String description, String imageUrl, int price, boolean available){

        validateInternalName(internalName);
        validateDisplayName(displayName);
        validatePrice(price);
        validateDescription(description);

        return Menu.builder()
                .id(id)
                .restaurantId(restaurantId)
                .internalName(internalName)
                .displayName(displayName)
                .description(description)
                .imageUrl(imageUrl)
                .price(price)
                .available(available)
                .build();
    }

    private static void validateInternalName(String internalName) {
        if (internalName.length() < 2 || internalName.length() > 50) {
            throw new InvalidMenuException("내부명은 2자 이상 50자 이하여야 합니다");
        }
    }

    private static void validateDisplayName(String displayName) {
        if (displayName.length() < 2 || displayName.length() > 50) {
            throw new InvalidMenuException("메뉴명은 2자 이상 50자 이하여야 합니다");
        }
    }

    private static void validatePrice(int price) {
        if (price < 1000 || price > 100_000) {
            throw new InvalidMenuException("메뉴 가격은 1,000원 이상 100,000원 이하여야 합니다");
        }
        if (price % 100 != 0) {
            throw new InvalidMenuException("메뉴 가격은 100원 단위여야 합니다");
        }
    }

    private static void validateDescription(String description) {
        if (description != null && description.length() > 500) {
            throw new InvalidMenuException("메뉴 설명은 500자를 초과할 수 없습니다");
        }
    }
}
