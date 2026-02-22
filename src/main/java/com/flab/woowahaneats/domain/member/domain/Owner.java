package com.flab.woowahaneats.domain.member.domain;

import com.flab.woowahaneats.domain.common.vo.Address;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class Owner extends Member {
    private Address address;
    private String businessRegistrationCertUrl;
    private String businessNotificationCertUrl;
    private String accountNumber;
    private String bankName;
}
