package com.flab.woowahaneats.domain.owner.domain;

import com.flab.woowahaneats.domain.common.vo.Address;
import com.flab.woowahaneats.domain.common.vo.BankAccount;
import com.flab.woowahaneats.domain.common.vo.Location;
import com.flab.woowahaneats.domain.owner.exception.InvalidOwnerException;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "owners")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "owner_id")
    private Long id;

    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Embedded
    @AttributeOverride(name = "detail", column = @Column(name = "address_detail"))
    private Address address;

    @Embedded
    private Location location;

    @Column(name = "business_registration_cert_url", nullable = false)
    private String businessRegistrationCertUrl;

    @Column(name = "business_notification_cert_url", nullable = false)
    private String businessNotificationCertUrl;

    @Embedded
    @AttributeOverride(name = "accountNumber", column = @Column(name = "account_number"))
    private BankAccount bankAccount;

    @Column(nullable = false)
    private String name;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    public static Owner create(
            Long accountId,
            String name,
            String phoneNumber,
            Address address,
            Location location,
            String businessRegistrationCertUrl,
            String businessNotificationCertUrl,
            BankAccount bankAccount
    ) {
        validateAccountId(accountId);
        validateName(name);
        validatePhoneNumber(phoneNumber);

        return Owner.builder()
                .accountId(accountId)
                .name(name)
                .phoneNumber(phoneNumber)
                .address(address)
                .location(location)
                .businessRegistrationCertUrl(businessRegistrationCertUrl)
                .businessNotificationCertUrl(businessNotificationCertUrl)
                .bankAccount(bankAccount)
                .build();
    }

    private static void validateAccountId(Long accountId) {
        if (accountId == null) {
            throw new InvalidOwnerException("계정 정보가 올바르지 않습니다.");
        }
    }

    private static void validateName(String name) {
        if (name.length() < 2 || name.length() > 50) {
            throw new InvalidOwnerException("이름은 2자 이상 50자 이하여야 합니다.");
        }
    }

    private static void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber.length() < 10 || phoneNumber.length() > 11) {
            throw new InvalidOwnerException("전화번호는 10자 이상 11자 이하여야 합니다.");
        }
    }
}
