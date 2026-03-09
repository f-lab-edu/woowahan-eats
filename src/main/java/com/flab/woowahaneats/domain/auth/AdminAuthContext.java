package com.flab.woowahaneats.domain.auth;

import com.flab.woowahaneats.domain.member.domain.Admin;

public class AdminAuthContext {

    private static final ThreadLocal<Admin> adminThreadLocal = new ThreadLocal<>();

    public static void setAdmin(Admin admin) {
        adminThreadLocal.set(admin);
    }

    public static Admin getAdmin() {
        return adminThreadLocal.get();
    }

    public static void clear(){
        adminThreadLocal.remove();
    }
}