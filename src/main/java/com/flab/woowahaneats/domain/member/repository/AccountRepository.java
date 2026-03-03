package com.flab.woowahaneats.domain.member.repository;

import com.flab.woowahaneats.domain.auth.domain.Account;

public interface AccountRepository {
    void save(Account account);
    Account findByEmail(String email);
    Account findById(Long id);
}