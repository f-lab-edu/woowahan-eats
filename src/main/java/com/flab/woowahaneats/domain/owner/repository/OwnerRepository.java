package com.flab.woowahaneats.domain.owner.repository;

import com.flab.woowahaneats.domain.owner.domain.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
    Owner findByAccountId(Long accountId);
}
