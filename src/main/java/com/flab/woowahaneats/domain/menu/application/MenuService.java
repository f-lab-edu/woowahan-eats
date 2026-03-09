package com.flab.woowahaneats.domain.menu.application;

import com.flab.woowahaneats.domain.auth.OwnerAuthContext;
import com.flab.woowahaneats.domain.member.domain.Owner;
import com.flab.woowahaneats.domain.menu.application.exception.MenuNotBelongToRestaurantException;
import com.flab.woowahaneats.domain.menu.application.exception.MenuNotFoundException;
import com.flab.woowahaneats.domain.menu.controller.dto.MenuRequest;
import com.flab.woowahaneats.domain.menu.controller.dto.MenuUpdateRequest;
import com.flab.woowahaneats.domain.menu.domain.Menu;
import com.flab.woowahaneats.domain.menu.repository.MenuRepository;
import com.flab.woowahaneats.domain.restaurant.application.exception.RestaurantNotFoundException;
import com.flab.woowahaneats.domain.restaurant.application.exception.RestaurantNotOwnedException;
import com.flab.woowahaneats.domain.restaurant.domain.Restaurant;
import com.flab.woowahaneats.domain.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    public void registerMenu(Long restaurantId, MenuRequest menuRequest) {

        validateRestaurantOwnership(restaurantId);

        Menu menu = Menu.create(
                menuRequest.id(),
                restaurantId,
                menuRequest.internalName(),
                menuRequest.displayName(),
                menuRequest.description(),
                menuRequest.images(),
                menuRequest.price(),
                menuRequest.available()
        );

        menuRepository.save(menu);
    }

    public void updateMenu(Long restaurantId, Long menuId, MenuUpdateRequest request) {

        validateRestaurantOwnership(restaurantId);

        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(MenuNotFoundException::new);

        if (!menu.getRestaurantId().equals(restaurantId)) {
            throw new MenuNotBelongToRestaurantException();
        }

        Menu updatedMenu = menu.update(
                request.internalName(),
                request.displayName(),
                request.description(),
                request.images(),
                request.price(),
                request.available()
        );

        menuRepository.save(updatedMenu);
    }

    public void deleteMenu(Long restaurantId, Long menuId) {

        validateRestaurantOwnership(restaurantId);

        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(MenuNotFoundException::new);

        if (!menu.getRestaurantId().equals(restaurantId)) {
            throw new MenuNotBelongToRestaurantException();
        }

        menuRepository.delete(menuId);
    }

    private void validateRestaurantOwnership(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(RestaurantNotFoundException::new);

        Owner owner = OwnerAuthContext.getOwner();

        if (!restaurant.getOwnerId().equals(owner.getId())) {
            throw new RestaurantNotOwnedException();
        }
    }
}
