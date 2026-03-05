package com.flab.woowahaneats.domain.member.repository;

import com.flab.woowahaneats.domain.member.domain.Admin;

public interface AdminRepository {
    void save(Admin admin);
    Admin findByAccountId(Long accountId);
}