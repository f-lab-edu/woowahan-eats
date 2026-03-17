package com.flab.woowahaneats.domain.cart.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Builder(toBuilder = true)
public class Cart {
    private UUID id;
    private Long userId;
    private Long restaurantId;
    private List<CartMenu> menus;

    public static Cart create(Long userId, Long restaurantId, List<CartMenu> menus) {
        return Cart.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .restaurantId(restaurantId)
                .menus(menus)
                .build();
    }

    public Cart updateRestaurant(Long restaurantId) {
        return this.toBuilder()
                .restaurantId(restaurantId)
                .build();
    }

    public Cart updateMenuQuantity(Long menuId, int quantity) {
        List<CartMenu> updatedMenus = this.menus.stream()
                .map(menu -> menu.menuId().equals(menuId)
                        ? new CartMenu(menuId, quantity)
                        : menu)
                .toList();

        return this.toBuilder()
                .menus(updatedMenus)
                .build();
    }

    public Cart removeMenu(Long menuId) {
        List<CartMenu> updatedMenus = this.menus.stream()
                .filter(menu -> !menu.menuId().equals(menuId))
                .toList();

        return this.toBuilder()
                .menus(updatedMenus)
                .build();
    }

    public Cart addMenu(CartMenu newMenu) {
        boolean menuExists = this.menus.stream()
                .anyMatch(menu -> menu.menuId().equals(newMenu.menuId()));

        List<CartMenu> updatedMenus;
        if (menuExists) {
            updatedMenus = this.menus.stream()
                    .map(menu -> menu.menuId().equals(newMenu.menuId())
                            ? new CartMenu(menu.menuId(), menu.quantity() + newMenu.quantity())
                            : menu)
                    .toList();
        } else {
            updatedMenus = new ArrayList<>(this.menus);
            updatedMenus.add(newMenu);
        }

        return this.toBuilder()
                .menus(updatedMenus)
                .build();
    }
}
