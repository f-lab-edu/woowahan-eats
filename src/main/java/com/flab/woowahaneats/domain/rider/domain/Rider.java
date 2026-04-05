package com.flab.woowahaneats.domain.rider.domain;

import com.flab.woowahaneats.domain.common.vo.BankAccount;
import com.flab.woowahaneats.domain.common.vo.Location;
import com.flab.woowahaneats.domain.rider.exception.InvalidRiderStatusException;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "riders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Rider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rider_id")
    private Long id;

    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Embedded
    private Location location;

    @Column(nullable = false)
    private String name;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Embedded
    @AttributeOverride(name = "accountNumber", column = @Column(name = "account_number"))
    private BankAccount bankAccount;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type", nullable = false)
    private VehicleType vehicleType;

    @Enumerated(EnumType.STRING)
    @Column(name = "rider_status", nullable = false)
    private RiderStatus riderStatus;

    public static Rider create(
            Long accountId,
            String name,
            String phoneNumber,
            Location location,
            BankAccount bankAccount,
            VehicleType vehicleType
    ) {
        return Rider.builder()
                .accountId(accountId)
                .name(name)
                .phoneNumber(phoneNumber)
                .location(location)
                .bankAccount(bankAccount)
                .vehicleType(vehicleType)
                .riderStatus(RiderStatus.RESTING)
                .build();
    }

    public void startWork() {
        if (this.riderStatus != RiderStatus.RESTING) {
            throw new InvalidRiderStatusException("이미 출근 상태입니다.");
        }
        this.riderStatus = RiderStatus.WAITING;
    }

    public void endWork() {
        if (this.riderStatus == RiderStatus.RESTING) {
            throw new InvalidRiderStatusException("이미 퇴근 상태입니다.");
        }
        if (this.riderStatus == RiderStatus.DELIVERING) {
            throw new InvalidRiderStatusException("배달 중에는 퇴근할 수 없습니다.");
        }
        this.riderStatus = RiderStatus.RESTING;
    }

    public void startDelivering() {
        if (this.riderStatus != RiderStatus.WAITING) {
            throw new InvalidRiderStatusException("대기 중인 라이더만 배달을 시작할 수 있습니다.");
        }
        this.riderStatus = RiderStatus.DELIVERING;
    }

    public void finishDelivering() {
        if (this.riderStatus != RiderStatus.DELIVERING) {
            throw new InvalidRiderStatusException("배달 중인 라이더만 배달을 완료할 수 있습니다.");
        }
        this.riderStatus = RiderStatus.WAITING;
    }
}
