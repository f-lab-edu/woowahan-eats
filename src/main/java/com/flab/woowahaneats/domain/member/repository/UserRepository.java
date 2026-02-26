package com.flab.woowahaneats.domain.member.repository;

import com.flab.woowahaneats.domain.member.domain.User;

public interface UserRepository {
    void save(User user);
    User findByAccountId(Long accountId);
}
