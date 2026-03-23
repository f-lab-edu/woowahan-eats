package com.flab.woowahaneats.domain.order.owner.repository;

import com.flab.woowahaneats.domain.order.owner.domain.OwnerOrder;

import java.util.Optional;
import java.util.UUID;

public interface OwnerOrderRepository {
    void save(OwnerOrder ownerOrder);
}
