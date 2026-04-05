package com.flab.woowahaneats.domain.auth.repository;

import com.flab.woowahaneats.domain.auth.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByEmail(String email);
}
