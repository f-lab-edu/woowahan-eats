package com.flab.woowahaneats.domain.menu.repository;

import com.flab.woowahaneats.domain.menu.domain.Menu;

import java.util.Optional;

public interface MenuRepository {
    void save(Menu menu);
    Optional<Menu> findById(Long id);
    void delete(Long id);
}
