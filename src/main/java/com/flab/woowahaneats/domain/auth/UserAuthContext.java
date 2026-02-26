package com.flab.woowahaneats.domain.auth;

import com.flab.woowahaneats.domain.member.domain.User;

public class UserAuthContext {

    private static final ThreadLocal<User> userThreadLocal = new ThreadLocal<>();

    public static void setUser(User user) {
        userThreadLocal.set(user);
    }

    public static User getUser() {
        return userThreadLocal.get();
    }

    public static void clear(){
        userThreadLocal.remove();
    }
}