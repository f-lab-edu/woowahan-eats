package com.flab.woowahaneats.domain.member.repository;


import com.flab.woowahaneats.domain.member.domain.Owner;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class OwnerRepository {
    HashMap<Long, Owner> ownerRepository = new HashMap<>();

    public void save(Owner owner) {
        ownerRepository.put(owner.getId(), owner);
    }
    public Owner findById(Long id) {
        return ownerRepository.get(id);
    }
    public Owner findByEmail(String email) {
        for (Owner owner : ownerRepository.values()) {
            if (owner.getEmail().equals(email)) {
                return owner;
            }
        }
        return null;
    }
}
