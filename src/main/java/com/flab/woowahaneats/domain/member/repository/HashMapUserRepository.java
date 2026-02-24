package com.flab.woowahaneats.domain.member.repository;

import com.flab.woowahaneats.domain.member.domain.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class HashMapUserRepository implements UserRepository {
    HashMap<Long, User> HashMapUserRepository = new HashMap<>();
    @Override
    public void save(User user) {
        HashMapUserRepository.put(user.getId(), user);
    }

    @Override
    public User findByAccountId(Long accountId) {
        for (User user : HashMapUserRepository.values()) {
            if (user.getAccountId().equals(accountId)) {
                return user;
            }
        }
        return null;
    }
}
