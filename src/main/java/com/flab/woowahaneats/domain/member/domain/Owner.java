package com.flab.woowahaneats.domain.member.domain;

import com.flab.woowahaneats.domain.common.vo.Address;
import com.flab.woowahaneats.domain.common.vo.BankAccount;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class Owner extends Member {
    private Address address;
    private String businessRegistrationCertUrl;
    private String businessNotificationCertUrl;
    private BankAccount bankAccount;
}
