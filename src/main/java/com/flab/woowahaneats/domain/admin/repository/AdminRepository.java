package com.flab.woowahaneats.domain.admin.repository;

import com.flab.woowahaneats.domain.admin.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findByAccountId(Long accountId);
}
