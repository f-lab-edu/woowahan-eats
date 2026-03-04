package com.flab.woowahaneats.domain.menu.application;

import com.flab.woowahaneats.domain.auth.OwnerAuthContext;
import com.flab.woowahaneats.domain.member.domain.Owner;
import com.flab.woowahaneats.domain.menu.controller.dto.MenuRequest;
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
                .name(menuRequest.name())
                .description(menuRequest.description())
                .price(menuRequest.price())
                .ImageUrl(menuRequest.ImageUrl())
                .isAvailable(menuRequest.isAvailable())
                .build();

        menuRepository.save(menu);
    }
}
