package com.flab.woowahaneats.domain.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Member {
    private Long id;
    private String name;
    private String businessNumber;
    private String bankName;
    private String accountNumber;
    private String password;
    private String email;
    private String phoneNumber;
    private String address;
}
