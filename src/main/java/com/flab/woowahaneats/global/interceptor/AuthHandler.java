package com.flab.woowahaneats.global.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthHandler {

    boolean handleAuth(HttpServletRequest request, HttpServletResponse response);
    boolean supports(HttpServletRequest request);
}
