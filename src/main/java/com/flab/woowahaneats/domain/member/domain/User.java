package com.flab.woowahaneats.domain.member.domain;

import com.flab.woowahaneats.domain.common.vo.Address;
import com.flab.woowahaneats.domain.common.vo.Location;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class User {
    private Long id;
    private Long accountId;
    private Address address;
    private Location location;
    private String profileImageUrl;
    private String NickName;
}
