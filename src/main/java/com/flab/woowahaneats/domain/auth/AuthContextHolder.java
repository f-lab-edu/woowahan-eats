package com.flab.woowahaneats.domain.auth;

public class AuthContextHolder {

    private static final ThreadLocal<AuthContext> contextHolder = new ThreadLocal<>();
    private AuthContextHolder() {
    }

    public static void setContext(AuthContext context) {
        contextHolder.set(context);
    }

    public static AuthContext getContext() {
        return contextHolder.get();
    }

    public static void clear() {
        contextHolder.remove();
    }
}