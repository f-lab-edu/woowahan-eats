package com.flab.woowahaneats.domain.member.repository;


import com.flab.woowahaneats.domain.member.domain.Owner;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class HashMapOwnerRepository implements OwnerRepository{
    HashMap<Long, Owner> HashMapOwnerRepository = new HashMap<>();

    public void save(Owner owner) {
        HashMapOwnerRepository.put(owner.getId(), owner);
    }
    public Owner findById(Long id) {
        return HashMapOwnerRepository.get(id);
    }
    public Owner findByAccountId(Long accountId) {
        for (Owner owner : HashMapOwnerRepository.values()) {
            if (owner.getAccountId().equals(accountId)) {
                return owner;
            }
        }
        return null;
    }
}
