package com.flab.woowahaneats.domain.menu.application;

import com.flab.woowahaneats.domain.menu.controller.dto.MenuRequest;
import com.flab.woowahaneats.domain.menu.controller.dto.MenuUpdateRequest;

public interface MenuService {

    void registerMenu(Long restaurantId, MenuRequest menuRequest);

    void updateMenu(Long restaurantId, Long menuId, MenuUpdateRequest request);

    void deleteMenu(Long restaurantId, Long menuId);
}
