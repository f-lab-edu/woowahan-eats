package com.flab.woowahaneats.domain.admin.domain;

import com.flab.woowahaneats.domain.admin.exception.InvalidAdminException;
import com.flab.woowahaneats.domain.auth.domain.Account;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "admins")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false, unique = true)
    private Account account;

    @Column(nullable = false)
    private String name;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    public static Admin create(Account account, String name, String phoneNumber) {
        validateAccount(account);
        validateName(name);
        validatePhoneNumber(phoneNumber);

        return Admin.builder()
                .account(account)
                .name(name)
                .phoneNumber(phoneNumber)
                .build();
    }

    private static void validateAccount(Account account) {
        if (account == null) {
            throw new InvalidAdminException("계정 정보가 올바르지 않습니다.");
        }
    }

    private static void validateName(String name) {
        if (name.length() < 2 || name.length() > 50) {
            throw new InvalidAdminException("이름은 2자 이상 50자 이하여야 합니다.");
        }
    }

    private static void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber.length() < 10 || phoneNumber.length() > 11) {
            throw new InvalidAdminException("전화번호는 10자 이상 11자 이하여야 합니다.");
        }
    }
}
