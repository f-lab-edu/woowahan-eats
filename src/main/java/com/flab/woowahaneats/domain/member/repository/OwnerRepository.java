package com.flab.woowahaneats.domain.member.repository;

import com.flab.woowahaneats.domain.member.domain.Owner;

public interface OwnerRepository {
    void save(Owner owner);
    Owner findById(Long id);
    Owner findByEmail(String email);
}
