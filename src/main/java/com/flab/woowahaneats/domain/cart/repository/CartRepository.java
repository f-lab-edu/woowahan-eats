package com.flab.woowahaneats.domain.cart.repository;

import com.flab.woowahaneats.domain.cart.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
