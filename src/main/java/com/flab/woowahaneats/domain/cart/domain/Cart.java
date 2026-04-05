package com.flab.woowahaneats.domain.cart.domain;

import com.flab.woowahaneats.domain.cart.exception.InvalidQuantityException;
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

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id", nullable = false)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "restaurant_id", nullable = false)
    private Long restaurantId;

    @ElementCollection
    @CollectionTable(name = "cart_menus", joinColumns = @JoinColumn(name = "cart_id"))
    private List<CartMenu> menus;

    public static Cart create(Long userId, Long restaurantId, List<CartMenu> menus) {
        menus.forEach(menu -> validateQuantity(menu.quantity()));
        return Cart.builder()
                .userId(userId)
                .restaurantId(restaurantId)
                .menus(menus)
                .build();
    }

    public Cart updateMenuQuantity(Long menuId, int quantity) {
        validateQuantity(quantity);
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
        validateQuantity(newMenu.quantity());
        boolean menuExists = this.menus.stream()
                .anyMatch(menu -> menu.menuId().equals(newMenu.menuId()));

        List<CartMenu> updatedMenus;
        if (menuExists) {
            updatedMenus = this.menus.stream()
                    .map(menu -> {
                        if (menu.menuId().equals(newMenu.menuId())) {
                            int totalQuantity = menu.quantity() + newMenu.quantity();
                            validateQuantity(totalQuantity);
                            return new CartMenu(menu.menuId(), totalQuantity);
                        }
                        return menu;
                    })
                    .toList();
        } else {
            updatedMenus = new ArrayList<>(this.menus);
            updatedMenus.add(newMenu);
        }

        return this.toBuilder()
                .menus(updatedMenus)
                .build();
    }

    private static void validateQuantity(int quantity) {
        if (quantity < 1 || quantity > 99) {
            throw new InvalidQuantityException();
        }
    }
}
