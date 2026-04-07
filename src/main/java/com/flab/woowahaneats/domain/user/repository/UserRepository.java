package com.flab.woowahaneats.domain.user.repository;

import com.flab.woowahaneats.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByAccountId(Long accountId);
}
