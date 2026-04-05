package com.flab.woowahaneats.domain.owner.domain;

import com.flab.woowahaneats.domain.common.vo.Address;
import com.flab.woowahaneats.domain.common.vo.BankAccount;
import com.flab.woowahaneats.domain.common.vo.Location;
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
}
