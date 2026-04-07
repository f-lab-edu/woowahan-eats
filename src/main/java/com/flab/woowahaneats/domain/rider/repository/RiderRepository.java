package com.flab.woowahaneats.domain.rider.repository;

import com.flab.woowahaneats.domain.rider.domain.Rider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RiderRepository extends JpaRepository<Rider, Long> {
    Rider findByAccountId(Long accountId);
}
