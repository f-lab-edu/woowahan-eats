package com.flab.woowahaneats.domain.auth;

import com.flab.woowahaneats.domain.member.domain.Owner;

public class OwnerAuthContext {

    private static final ThreadLocal<Owner> ownerThreadLocal = new ThreadLocal<>();

    public static void setOwner(Owner owner) {
        ownerThreadLocal.set(owner);
    }

    public static Owner getOwner() {
        return ownerThreadLocal.get();
    }

    public static void clear(){
        ownerThreadLocal.remove();
    }
}
