package com.flab.woowahaneats.domain.admin.application;

import com.flab.woowahaneats.domain.admin.controller.dto.AdminSignUpRequest;

public interface AdminService {

    void signUpAdmin(AdminSignUpRequest adminSignUpRequest);
}
