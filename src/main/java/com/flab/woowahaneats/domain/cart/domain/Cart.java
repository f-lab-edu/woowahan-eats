package com.flab.woowahaneats.domain.cart.domain;

import com.flab.woowahaneats.domain.cart.exception.InvalidQuantityException;
import com.flab.woowahaneats.domain.restaurant.domain.Restaurant;
import com.flab.woowahaneats.domain.user.domain.User;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SoftDelete;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@SoftDelete(columnName = "deleted")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id", nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @ElementCollection
    @CollectionTable(name = "cart_menus", joinColumns = @JoinColumn(name = "cart_id"))
    private List<CartMenu> menus;

    public static Cart create(User user, Restaurant restaurant, List<CartMenu> cartMenus) {
        cartMenus.forEach(menu -> validateQuantity(menu.quantity()));
        return Cart.builder()
                .user(user)
                .restaurant(restaurant)
                .menus(new ArrayList<>(cartMenus))
                .build();
    }

    public void updateMenuQuantity(Long menuId, int quantity) {
        validateQuantity(quantity);
        for (int i = 0; i < this.menus.size(); i++) {
            CartMenu menu = this.menus.get(i);
            if (menu.menuId().equals(menuId)) {
                this.menus.set(i, new CartMenu(menuId, quantity));
            }
        }
    }

    public void removeMenu(Long menuId) {
        this.menus.removeIf(menu -> menu.menuId().equals(menuId));
    }

    public void addMenu(CartMenu newMenu) {
        validateQuantity(newMenu.quantity());
        for (int i = 0; i < this.menus.size(); i++) {
            CartMenu menu = this.menus.get(i);
            if (menu.menuId().equals(newMenu.menuId())) {
                int totalQuantity = menu.quantity() + newMenu.quantity();
                validateQuantity(totalQuantity);
                this.menus.set(i, new CartMenu(menu.menuId(), totalQuantity));
                return;
            }
        }
        this.menus.add(newMenu);
    }

    private static void validateQuantity(int quantity) {
        if (quantity < 1 || quantity > 99) {
            throw new InvalidQuantityException();
        }
    }
}
