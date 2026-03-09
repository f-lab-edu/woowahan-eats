package com.flab.woowahaneats.domain.menu.repository;

import com.flab.woowahaneats.domain.menu.domain.Menu;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Optional;

@Repository
public class HashMapMenuRepository implements MenuRepository {
    private final HashMap<Long, Menu> hashMapMenuRepository = new HashMap<>();

    @Override
    public void save(Menu menu) {
        hashMapMenuRepository.put(menu.getId(), menu);
    }

    @Override
    public Optional<Menu> findById(Long id) {
        return Optional.ofNullable(hashMapMenuRepository.get(id));
    }

    @Override
    public void delete(Long id) {
        hashMapMenuRepository.remove(id);
    }
}
