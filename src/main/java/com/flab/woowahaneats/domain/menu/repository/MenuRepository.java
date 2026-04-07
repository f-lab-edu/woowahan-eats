package com.flab.woowahaneats.domain.menu.repository;

import com.flab.woowahaneats.domain.menu.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Query("SELECT m FROM Menu m JOIN FETCH m.restaurant WHERE m.id IN :menuIds")
    List<Menu> findAllByIdWithRestaurant(@Param("menuIds") List<Long> menuIds);
}
