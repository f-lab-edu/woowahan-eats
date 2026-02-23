package com.flab.woowahaneats.domain.member.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Account {
    private Long id;
    private String name;
    private String password;
    private String email;
    private String phoneNumber;
}
