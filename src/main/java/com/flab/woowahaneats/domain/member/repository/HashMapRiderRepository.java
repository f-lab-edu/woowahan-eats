package com.flab.woowahaneats.domain.member.repository;

import com.flab.woowahaneats.domain.member.domain.Rider;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Optional;

@Repository
public class HashMapRiderRepository implements RiderRepository {
    HashMap<Long, Rider> HashMapRiderRepository = new HashMap<>();

    @Override
    public void save(Rider rider) {
        HashMapRiderRepository.put(rider.getId(), rider);
    }

    @Override
    public Rider findByAccountId(Long accountId) {
        for (Rider rider : HashMapRiderRepository.values()) {
            if (rider.getAccountId().equals(accountId)) {
                return rider;
            }
        }
        return null;
    }

    @Override
    public Optional<Rider> findById(Long riderId) {
        return Optional.ofNullable(HashMapRiderRepository.get(riderId));
    }
}