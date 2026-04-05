package com.flab.woowahaneats.domain.owner.repository;

import com.flab.woowahaneats.domain.owner.domain.Owner;

public interface OwnerRepository {
    void save(Owner owner);
    Owner findById(Long id);
    Owner findByAccountId(Long accountId);
}