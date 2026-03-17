package com.flab.woowahaneats.domain.cart.application;

import com.flab.woowahaneats.domain.cart.application.exception.InvalidQuantityException;
import com.flab.woowahaneats.domain.cart.domain.Cart;
import com.flab.woowahaneats.domain.cart.domain.CartMenu;
import com.flab.woowahaneats.domain.cart.repository.CartRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CartService 테스트")
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartService cartService;

    @Nested
    @DisplayName("장바구니 생성")
    class CreateCart {

        @Test
        @DisplayName("유효한 데이터로 장바구니를 생성하면 Cart 객체가 올바르게 생성되고 저장된다")
        void createCart_WithValidData_SavesCartCorrectly() {
            // Given
            Long userId = 1L;
            Long restaurantId = 100L;
            List<CartMenu> menus = List.of(
                    new CartMenu(1L, 2),
                    new CartMenu(2L, 3)
            );

            // When
            cartService.createCart(userId, restaurantId, menus);

            // Then
            ArgumentCaptor<Cart> cartCaptor = ArgumentCaptor.forClass(Cart.class);
            verify(cartRepository).save(cartCaptor.capture());

            Cart savedCart = cartCaptor.getValue();
            assertThat(savedCart).isNotNull();
            assertThat(savedCart.getId()).isNotNull();
            assertThat(savedCart.getUserId()).isEqualTo(userId);
            assertThat(savedCart.getRestaurantId()).isEqualTo(restaurantId);
            assertThat(savedCart.getMenus())
                    .hasSize(2)
                    .extracting(CartMenu::menuId)
                    .containsExactly(1L, 2L);
        }

        @Test
        @DisplayName("수량이 0인 메뉴가 포함되면 InvalidQuantityException이 발생한다")
        void createCart_WithZeroQuantity_ThrowsException() {
            // Given
            Long userId = 1L;
            Long restaurantId = 100L;
            List<CartMenu> menus = List.of(
                    new CartMenu(1L, 0)
            );

            // When & Then
            assertThatThrownBy(() -> cartService.createCart(userId, restaurantId, menus))
                    .isInstanceOf(InvalidQuantityException.class)
                    .hasMessage("수량은 1개 이상 99개 이하여야 합니다.");

            verify(cartRepository, never()).save(any(Cart.class));
        }

        @Test
        @DisplayName("수량이 100인 메뉴가 포함되면 InvalidQuantityException이 발생한다")
        void createCart_WithOverMaxQuantity_ThrowsException() {
            // Given
            Long userId = 1L;
            Long restaurantId = 100L;
            List<CartMenu> menus = List.of(
                    new CartMenu(1L, 100)
            );

            // When & Then
            assertThatThrownBy(() -> cartService.createCart(userId, restaurantId, menus))
                    .isInstanceOf(InvalidQuantityException.class)
                    .hasMessage("수량은 1개 이상 99개 이하여야 합니다.");

            verify(cartRepository, never()).save(any(Cart.class));
        }

    }
}