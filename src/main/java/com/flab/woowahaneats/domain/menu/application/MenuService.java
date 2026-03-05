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

        Owner owner = OwnerAuthContext.getOwner();

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(RestaurantNotFoundException::new);

        if(!restaurant.getOwnerId().equals(owner.getId())) {
            throw new RestaurantNotOwnedException();
        }

        Menu menu = Menu.builder()
                .id(menuRequest.id())
                .restaurantId(restaurantId)
                .internalName(menuRequest.internalName())
                .displayName(menuRequest.displayName())
                .description(menuRequest.description())
                .price(menuRequest.price())
                .imageUrl(menuRequest.imageUrl())
                .available(menuRequest.available())
                .build();

        menuRepository.save(menu);
    }

    public void updateMenu(Long restaurantId, Long menuId, MenuUpdateRequest request) {

        Owner owner = OwnerAuthContext.getOwner();

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(RestaurantNotFoundException::new);

        if (!restaurant.getOwnerId().equals(owner.getId())) {
            throw new RestaurantNotOwnedException();
        }

        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(MenuNotFoundException::new);

        if (!menu.getRestaurantId().equals(restaurantId)) {
            throw new MenuNotBelongToRestaurantException();
        }

        Menu updatedMenu = menu.toBuilder()
                .internalName(request.internalName() != null ? request.internalName() : menu.getInternalName())
                .displayName(request.displayName() != null ? request.displayName() : menu.getDisplayName())
                .description(request.description() != null ? request.description() : menu.getDescription())
                .imageUrl(request.imageUrl() != null ? request.imageUrl() : menu.getImageUrl())
                .price(request.price() != null ? request.price() : menu.getPrice())
                .available(request.available() != null ? request.available() : menu.isAvailable())
                .build();

        menuRepository.save(updatedMenu);
    }
}
