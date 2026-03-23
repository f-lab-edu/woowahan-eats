package com.flab.woowahaneats.domain.order.owner.repository;

import com.flab.woowahaneats.domain.order.owner.domain.OwnerOrder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.UUID;

@Repository
public class HashMapOwnerOrderRepository implements OwnerOrderRepository {

    HashMap<UUID, OwnerOrder> ownerOrderMap = new HashMap<>();

    @Override
    public void save(OwnerOrder ownerOrder) {
        ownerOrderMap.put(ownerOrder.getId(), ownerOrder);
    }
}
