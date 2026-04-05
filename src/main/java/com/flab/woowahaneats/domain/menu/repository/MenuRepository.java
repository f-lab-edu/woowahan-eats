package com.flab.woowahaneats.domain.menu.repository;

import com.flab.woowahaneats.domain.menu.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
