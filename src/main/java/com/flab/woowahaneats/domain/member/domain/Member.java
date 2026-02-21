package com.flab.woowahaneats.domain.member.domain;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public abstract class Member {
    private Long id;
    private String name;
    private String password;
    private String email;
    private String phoneNumber;
}
