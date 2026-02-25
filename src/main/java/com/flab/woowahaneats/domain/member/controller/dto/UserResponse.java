package com.flab.woowahaneats.domain.member.controller.dto;

import com.flab.woowahaneats.domain.common.vo.Address;
import com.flab.woowahaneats.domain.member.domain.Account;
import com.flab.woowahaneats.domain.member.domain.User;

public record UserResponse (
        String name,

        String email,

        String phoneNumber,

        Address address,

        String profileImageUrl,

        String nickName
){
    public static UserResponse from(User user, Account account) {
        return new UserResponse(
                account.getName(),
                account.getEmail(),
                account.getPhoneNumber(),
                user.getAddress(),
                user.getProfileImageUrl(),
                user.getNickName()
        );
    }
}
