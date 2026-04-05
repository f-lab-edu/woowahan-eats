package com.flab.woowahaneats.domain.menu.domain;

import com.flab.woowahaneats.domain.menu.exception.InvalidMenuException;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "menus")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long id;

    @Column(name = "restaurant_id", nullable = false)
    private Long restaurantId;

    @Column(name = "internal_name", nullable = false)
    private String internalName;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    private String description;

    @ElementCollection
    @CollectionTable(name = "menu_images", joinColumns = @JoinColumn(name = "menu_id"))
    private List<MenuImage> images;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private boolean available;

    public Menu update(String internalName, String displayName, String description,
                       List<MenuImage> images, Integer price, Boolean available) {
        return this.toBuilder()
                .internalName(internalName != null ? internalName : this.internalName)
                .displayName(displayName != null ? displayName : this.displayName)
                .description(description != null ? description : this.description)
                .images(images != null ? images : this.images)
                .price(price != null ? price : this.price)
                .available(available != null ? available : this.available)
                .build();
    }

    public static Menu create(Long restaurantId, String internalName, String displayName,
                              String description, List<MenuImage> images, int price, boolean available){

        validateInternalName(internalName);
        validateDisplayName(displayName);
        validatePrice(price);
        validateDescription(description);
        validateImages(images);

        return Menu.builder()
                .restaurantId(restaurantId)
                .internalName(internalName)
                .displayName(displayName)
                .description(description)
                .images(images)
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

    private static void validateImages(List<MenuImage> images) {
        if (images == null || images.isEmpty()) {
            return;
        }

        long mainImageCount = images.stream()
                .filter(MenuImage::main)
                .count();

        if (mainImageCount != 1) {
            throw new InvalidMenuException("대표 이미지는 1개여야 합니다");
        }
    }
}
