package com.flab.woowahaneats.domain.auth.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Account {
    private Long id;
    private String password;
    private String email;
}
