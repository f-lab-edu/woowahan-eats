package com.flab.woowahaneats.domain.user.application;

import com.flab.woowahaneats.domain.user.controller.dto.UserResponse;
import com.flab.woowahaneats.domain.user.controller.dto.UserSignUpRequest;

public interface UserService {

    void signUpUser(UserSignUpRequest userSignUpRequest);

    UserResponse getUserProfile();
}