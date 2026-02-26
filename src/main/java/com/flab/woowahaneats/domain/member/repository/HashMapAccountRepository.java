package com.flab.woowahaneats.domain.member.repository;

import com.flab.woowahaneats.domain.member.domain.Account;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class HashMapAccountRepository implements AccountRepository {
    HashMap<Long, Account> HashMapAccountRepository = new HashMap<>();
    public void save(Account account) {
        HashMapAccountRepository.put(account.getId(), account);
    }

    public Account findByEmail(String email) {
        for (Account account : HashMapAccountRepository.values()) {
            if (account.getEmail().equals(email)) {
                return account;
            }
        }
        return null;
    }

    public Account findById(Long id) {
        return HashMapAccountRepository.get(id);
    }
}
