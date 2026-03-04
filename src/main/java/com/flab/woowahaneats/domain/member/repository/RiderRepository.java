package com.flab.woowahaneats.domain.member.repository;

import com.flab.woowahaneats.domain.member.domain.Rider;

public interface RiderRepository {
    void save(Rider rider);
    Rider findByAccountId(Long accountId);
}