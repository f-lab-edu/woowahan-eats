package com.flab.woowahaneats.domain.member.domain;

import com.flab.woowahaneats.domain.common.vo.Address;
import com.flab.woowahaneats.domain.common.vo.BankAccount;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Owner {
    private Long id;
    private Long accountId;
    private Address address;
    private String businessRegistrationCertUrl;
    private String businessNotificationCertUrl;
    private BankAccount bankAccount;
}
