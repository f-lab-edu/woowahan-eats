package com.flab.woowahaneats.domain.member.domain;

import com.flab.woowahaneats.domain.common.vo.BankAccount;
import com.flab.woowahaneats.domain.common.vo.Location;
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
}
