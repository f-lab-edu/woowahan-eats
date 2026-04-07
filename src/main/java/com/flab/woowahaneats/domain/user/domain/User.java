package com.flab.woowahaneats.domain.user.domain;

import com.flab.woowahaneats.domain.auth.domain.Account;
import com.flab.woowahaneats.domain.common.vo.Address;
import com.flab.woowahaneats.domain.common.vo.Location;
import com.flab.woowahaneats.domain.user.exception.InvalidUserException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false, unique = true)
    private Account account;

    @Embedded
    @AttributeOverride(name = "detail", column = @Column(name = "address_detail"))
    private Address address;

    @Embedded
    private Location location;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "nick_name")
    private String nickName;

    @Column(nullable = false)
    private String name;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    public static User create(
            Account account,
            String name,
            String phoneNumber,
            Address address,
            Location location,
            String profileImageUrl,
            String nickName
    ) {
        validateAccount(account);
        validateName(name);
        validatePhoneNumber(phoneNumber);
        validateNickName(nickName);

        return User.builder()
                .account(account)
                .name(name)
                .phoneNumber(phoneNumber)
                .address(address)
                .location(location)
                .profileImageUrl(profileImageUrl)
                .nickName(nickName)
                .build();
    }

    private static void validateAccount(Account account) {
        if (account == null) {
            throw new InvalidUserException("계정 정보가 올바르지 않습니다.");
        }
    }

    private static void validateName(String name) {
        if (name.length() < 2 || name.length() > 50) {
            throw new InvalidUserException("이름은 2자 이상 50자 이하여야 합니다.");
        }
    }

    private static void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber.length() < 10 || phoneNumber.length() > 11) {
            throw new InvalidUserException("전화번호는 10자 이상 11자 이하여야 합니다.");
        }
    }

    private static void validateNickName(String nickName) {
        if (nickName == null || nickName.isBlank()) {
            return;
        }
        if (nickName.length() < 2 || nickName.length() > 30) {
            throw new InvalidUserException("닉네임은 2자 이상 30자 이하여야 합니다.");
        }
    }
}
