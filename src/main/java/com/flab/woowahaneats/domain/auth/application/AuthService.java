package com.flab.woowahaneats.domain.auth.application;

import com.flab.woowahaneats.domain.auth.controller.dto.AuthLoginRequest;
import com.flab.woowahaneats.domain.auth.domain.Account;

public interface AuthService {

    Account login(AuthLoginRequest authLoginRequest);
}
