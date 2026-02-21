package com.flab.woowahaneats.domain.member.domain;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class Owner extends Member {
    private String address;
    private String businessNumber;
    private String accountNumber;
    private String bankName;
}
