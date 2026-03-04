package com.flab.woowahaneats.domain.auth;

import com.flab.woowahaneats.domain.member.domain.Rider;

public class RiderAuthContext {

    private static final ThreadLocal<Rider> riderThreadLocal = new ThreadLocal<>();

    public static void setRider(Rider rider) {
        riderThreadLocal.set(rider);
    }

    public static Rider getRider() {
        return riderThreadLocal.get();
    }

    public static void clear(){
        riderThreadLocal.remove();
    }
}