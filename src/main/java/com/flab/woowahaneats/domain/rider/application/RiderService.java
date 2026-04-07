package com.flab.woowahaneats.domain.rider.application;

import com.flab.woowahaneats.domain.rider.controller.dto.RiderSignUpRequest;

public interface RiderService {

    void signUpRider(RiderSignUpRequest riderSignUpRequest);
    void startWork(Long riderId);
    void endWork(Long riderId);
    void startDelivering(Long riderId);
    void finishDelivering(Long riderId);
}
