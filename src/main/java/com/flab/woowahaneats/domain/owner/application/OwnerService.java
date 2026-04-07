package com.flab.woowahaneats.domain.owner.application;

import com.flab.woowahaneats.domain.owner.controller.dto.OwnerSignUpRequest;

public interface OwnerService {

    void signUpOwner(OwnerSignUpRequest ownerSignUpRequest);
}
