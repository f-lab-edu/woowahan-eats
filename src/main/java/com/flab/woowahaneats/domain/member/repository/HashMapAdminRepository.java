package com.flab.woowahaneats.domain.member.repository;

import com.flab.woowahaneats.domain.member.domain.Admin;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class HashMapAdminRepository implements AdminRepository {
    HashMap<Long, Admin> AdminMap = new HashMap<>();

    @Override
    public void save(Admin admin) {
        AdminMap.put(admin.getId(), admin);
    }

    @Override
    public Admin findByAccountId(Long accountId) {
        for (Admin admin : AdminMap.values()) {
            if (admin.getAccountId().equals(accountId)) {
                return admin;
            }
        }
        return null;
    }
}