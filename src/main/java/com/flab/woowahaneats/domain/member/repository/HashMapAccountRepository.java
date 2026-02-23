package com.flab.woowahaneats.domain.member.repository;

import com.flab.woowahaneats.domain.member.domain.Account;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class HashMapAccountRepository implements AccountRepository {
    HashMap<Long, Account> accountRepository = new HashMap<>();
    public void save(Account account) {
        accountRepository.put(account.getId(), account);
    }

    public Account findById(Long id) {
        return accountRepository.get(id);
    }

    public Account findByEmail(String email) {
        for (Account account : accountRepository.values()) {
            if (account.getEmail().equals(email)) {
                return account;
            }
        }
        return null;
    }
}
