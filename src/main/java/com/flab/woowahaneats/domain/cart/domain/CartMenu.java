package com.flab.woowahaneats.domain.cart.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public record CartMenu (
        Long menuId,
        int quantity
){}
