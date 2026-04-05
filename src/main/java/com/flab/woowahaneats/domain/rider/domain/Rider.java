package com.flab.woowahaneats.domain.rider.domain;

import com.flab.woowahaneats.domain.common.vo.BankAccount;
import com.flab.woowahaneats.domain.common.vo.Location;
import com.flab.woowahaneats.domain.rider.exception.InvalidRiderStatusException;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Rider {
    private Long id;
    private Long accountId;
    private Location location;
    private String name;
    private String phoneNumber;
    private BankAccount bankAccount;
    private VehicleType vehicleType;
    private RiderStatus riderStatus;

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