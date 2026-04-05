package com.flab.woowahaneats.domain.rider.repository;

import com.flab.woowahaneats.domain.rider.domain.Rider;

import java.util.Optional;

public interface RiderRepository {
    void save(Rider rider);
    Rider findByAccountId(Long accountId);
    Optional<Rider> findById(Long riderId);
}