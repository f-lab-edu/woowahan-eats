package com.flab.woowahaneats.domain.member.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Admin {
    private Long id;
    private Long accountId;
    private String name;
    private String phoneNumber;
}
