package com.flab.woowahaneats.domain.member.repository;

import com.flab.woowahaneats.domain.member.domain.Account;

public interface AccountRepository {
    void save(Account account);
    Account findByEmail(String email);
}