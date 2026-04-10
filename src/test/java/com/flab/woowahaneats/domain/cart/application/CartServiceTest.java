package com.flab.woowahaneats.domain.cart.application;

import com.flab.woowahaneats.domain.auth.AuthContext;
import com.flab.woowahaneats.domain.auth.AuthContextHolder;
import com.flab.woowahaneats.domain.cart.exception.CartNotFoundException;
import com.flab.woowahaneats.domain.cart.exception.InvalidQuantityException;
import com.flab.woowahaneats.domain.cart.domain.Cart;
import com.flab.woowahaneats.domain.cart.domain.CartMenu;
import com.flab.woowahaneats.domain.cart.repository.CartRepository;
import com.flab.woowahaneats.domain.restaurant.domain.Restaurant;
import com.flab.woowahaneats.domain.restaurant.repository.RestaurantRepository;
import com.flab.woowahaneats.domain.user.domain.User;
import com.flab.woowahaneats.domain.menu.repository.MenuRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CartService 테스트")
class CartServiceTest {

    private static final Long USER_ID = 1L;
    private static final Long RESTAURANT_ID = 100L;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    private CartServiceImpl cartService;
    private User mockUser;
    private Restaurant mockRestaurant;

    @BeforeEach
    void setUp() {
        cartService = new CartServiceImpl(cartRepository, menuRepository, restaurantRepository);
        mockUser = User.builder()
                .id(USER_ID)
                .name("테스트 사용자")
                .phoneNumber("01012345678")
                .build();
        mockRestaurant = Restaurant.builder()
                .id(RESTAURANT_ID)
                .name("테스트 식당")
                .description("설명")
                .build();
        AuthContext authContext = new AuthContext(mockUser, "USER");
        AuthContextHolder.setContext(authContext);
    }

    @AfterEach
    void tearDown() {
        AuthContextHolder.clear();
    }

    @Nested
    @DisplayName("장바구니 생성")
    class CreateCart {

        @Test
        @DisplayName("유효한 데이터로 장바구니를 생성하면 Cart 객체가 올바르게 생성되고 저장된다")
        void createCart_WithValidData_SavesCartCorrectly() {
            // Given
            Long restaurantId = 100L;
            List<CartMenu> menus = List.of(
                    new CartMenu(1L, 2),
                    new CartMenu(2L, 3)
            );
            when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(mockRestaurant));

            // When
            cartService.createCart(restaurantId, menus);

            // Then
            ArgumentCaptor<Cart> cartCaptor = ArgumentCaptor.forClass(Cart.class);
            verify(cartRepository).save(cartCaptor.capture());

            Cart savedCart = cartCaptor.getValue();
            assertThat(savedCart).isNotNull();
            assertThat(savedCart.getUser().getId()).isEqualTo(USER_ID);
            assertThat(savedCart.getRestaurant().getId()).isEqualTo(restaurantId);
            assertThat(savedCart.getMenus())
                    .hasSize(2)
                    .extracting(CartMenu::menuId)
                    .containsExactly(1L, 2L);
        }

        @Test
        @DisplayName("수량이 0인 메뉴가 포함되면 InvalidQuantityException이 발생한다")
        void createCart_WithZeroQuantity_ThrowsException() {
            // Given
            Long restaurantId = 100L;
            List<CartMenu> menus = List.of(
                    new CartMenu(1L, 0)
            );
            when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(mockRestaurant));

            // When & Then
            assertThatThrownBy(() -> cartService.createCart(restaurantId, menus))
                    .isInstanceOf(InvalidQuantityException.class)
                    .hasMessage("수량은 1개 이상 99개 이하여야 합니다.");

            verify(cartRepository, never()).save(any(Cart.class));
        }

        @Test
        @DisplayName("수량이 100인 메뉴가 포함되면 InvalidQuantityException이 발생한다")
        void createCart_WithOverMaxQuantity_ThrowsException() {
            // Given
            Long restaurantId = 100L;
            List<CartMenu> menus = List.of(
                    new CartMenu(1L, 100)
            );
            when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(mockRestaurant));

            // When & Then
            assertThatThrownBy(() -> cartService.createCart(restaurantId, menus))
                    .isInstanceOf(InvalidQuantityException.class)
                    .hasMessage("수량은 1개 이상 99개 이하여야 합니다.");

            verify(cartRepository, never()).save(any(Cart.class));
        }

    }

    @Nested
    @DisplayName("장바구니 조회")
    class GetCart {

        @Test
        @DisplayName("존재하는 장바구니 ID로 조회하면 장바구니 정보를 반환한다")
        void getCart_WithExistingCartId_ReturnsCart(){

            // Given
            Long cartId = 1L;
            Long userId = USER_ID;
            Long restaurantId = 100L;
            List<CartMenu> menus = List.of(new CartMenu(1L, 2));

            Cart mockCart = Cart.builder()
                    .id(cartId)
                    .user(mockUser)
                    .restaurant(mockRestaurant)
                    .menus(menus)
                    .build();

            when(cartRepository.findById(cartId)).thenReturn(Optional.of(mockCart));

            // When
            Cart result = cartService.getCart(cartId);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(cartId);
            assertThat(result.getUser().getId()).isEqualTo(userId);
            assertThat(result.getRestaurant().getId()).isEqualTo(restaurantId);
            assertThat(result.getMenus()).hasSize(1);

            verify(cartRepository).findById(cartId);
        }

        @Test
        @DisplayName("존재하지 않는 장바구니 ID로 조회하면 CartNotFoundException이 발생한다")
        void getCart_WithNonExistingCartId_ThrowsException() {
            // Given
            Long cartId = 1L;

            when(cartRepository.findById(cartId)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> cartService.getCart(cartId))
            .isInstanceOf(CartNotFoundException.class).hasMessage("장바구니를 찾을 수 없습니다.");

            verify(cartRepository).findById(cartId);
        }
    }

    @Nested
    @DisplayName("장바구니 메뉴 수량 수정")
    class UpdateCartMenu{

        @Test
        @DisplayName("유효한 수량으로 메뉴 수량을 수정하면 정상적으로 업데이트된다")
        void updateMenuQuantity_WithValidQuantity_UpdatesSuccessfully() {

            // Given
            Long cartId = 1L;
            Long menuId = 1L;
            int newQuantity = 5;

            Cart mockCart = Cart.builder()
                    .id(cartId)
                    .user(mockUser)
                    .restaurant(mockRestaurant)
                    .menus(List.of(new CartMenu(menuId, 2)))
                    .build();

            when(cartRepository.findById(cartId)).thenReturn(Optional.of(mockCart));

            // When
            cartService.updateMenuQuantity(cartId, menuId, newQuantity);

            // Then
            ArgumentCaptor<Cart> cartCaptor = ArgumentCaptor.forClass(Cart.class);
            verify(cartRepository).save(cartCaptor.capture());

            Cart savedCart = cartCaptor.getValue();
            assertThat(savedCart.getMenus())
                    .hasSize(1)
                    .extracting(CartMenu::quantity)
                    .containsExactly(newQuantity);
        }
    }

    @Nested
    @DisplayName("장바구니 메뉴 삭제")
    class DeleteCart{

        @Test
        @DisplayName("장바구니에서 메뉴를 삭제하면 해당 메뉴가 제거된다.")
        void deleteCartMenu_WithValidMenu_RemovesSuccessfully() {
            // Given
            Long cartId = 1L;
            Long menuIdToDelete = 1L;

            Cart mockCart = Cart.builder()
                    .id(cartId)
                    .user(mockUser)
                    .restaurant(mockRestaurant)
                    .menus(List.of(
                            new CartMenu(1L, 2),
                            new CartMenu(2L, 3)
                    ))
                    .build();

            when(cartRepository.findById(cartId)).thenReturn(Optional.of(mockCart));

            // When
            cartService.deleteCartMenu(cartId, menuIdToDelete);

            // Then
            ArgumentCaptor<Cart> cartCaptor = ArgumentCaptor.forClass(Cart.class);
            verify(cartRepository).save(cartCaptor.capture());

            Cart savedCart = cartCaptor.getValue();
            assertThat(savedCart.getMenus())
                    .hasSize(1)
                    .extracting(CartMenu::menuId)
                    .containsExactly(2L);
        }

        @Test
        @DisplayName("장바구니를 삭제하면 정상적으로 삭제된다")
        void deleteCart_WithValidCart_DeletesSuccessfully() {
            // Given
            Long cartId = 1L;
            Cart mockCart = Cart.builder()
                    .id(cartId)
                    .user(mockUser)
                    .restaurant(mockRestaurant)
                    .menus(List.of(new CartMenu(1L, 2)))
                    .build();

            when(cartRepository.findById(cartId)).thenReturn(Optional.of(mockCart));

            // When
            cartService.deleteCart(cartId);

            // Then
            verify(cartRepository).delete(mockCart);
        }
    }
}
