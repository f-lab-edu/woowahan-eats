package com.flab.woowahaneats.domain.auth;

import com.flab.woowahaneats.domain.auth.exception.InvalidRoleException;
import com.flab.woowahaneats.domain.member.domain.Admin;
import com.flab.woowahaneats.domain.member.domain.Owner;
import com.flab.woowahaneats.domain.member.domain.Rider;
import com.flab.woowahaneats.domain.member.domain.User;

public class AuthContext {

    private final Object principal;
    private final String role;

    public AuthContext(Object principal, String role) {
        this.principal = principal;
        this.role = role;
    }

    public User getUser() {
        if (!"USER".equals(role)) {
            throw new InvalidRoleException("USER");
        }
        return (User) principal;
    }

    public Admin getAdmin() {
        if (!"ADMIN".equals(role)) {
            throw new InvalidRoleException("ADMIN");
        }
        return (Admin) principal;
    }

    public Owner getOwner() {
        if (!"OWNER".equals(role)) {
            throw new InvalidRoleException("OWNER");
        }
        return (Owner) principal;
    }

    public Rider getRider() {
        if (!"RIDER".equals(role)) {
            throw new InvalidRoleException("RIDER");
        }
        return (Rider) principal;
    }
}