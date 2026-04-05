package com.flab.woowahaneats.domain.user.repository;

import com.flab.woowahaneats.domain.user.domain.User;

public interface UserRepository {
    void save(User user);
    User findByAccountId(Long accountId);
}