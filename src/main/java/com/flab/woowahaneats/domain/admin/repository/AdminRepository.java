package com.flab.woowahaneats.domain.admin.repository;

import com.flab.woowahaneats.domain.admin.domain.Admin;

public interface AdminRepository {
    void save(Admin admin);
    Admin findByAccountId(Long accountId);
}