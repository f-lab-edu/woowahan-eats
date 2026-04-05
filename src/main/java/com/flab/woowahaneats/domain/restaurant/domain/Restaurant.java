package com.flab.woowahaneats.domain.restaurant.domain;

import com.flab.woowahaneats.domain.common.vo.Address;
import com.flab.woowahaneats.domain.common.vo.Location;
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
@Table(name = "restaurants")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_id")
    private Long id;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Embedded
    @AttributeOverride(name = "detail", column = @Column(name = "address_detail"))
    private Address address;

    @Embedded
    private Location location;

    @Column(nullable = false)
    private double avgRating;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status", nullable = false)
    private RestaurantApprovalStatus approvalStatus;

    public static Restaurant create(
            Long ownerId,
            String name,
            String description,
            Address address,
            Location location
    ) {
        return Restaurant.builder()
                .ownerId(ownerId)
                .name(name)
                .description(description)
                .address(address)
                .location(location)
                .avgRating(0.0)
                .approvalStatus(RestaurantApprovalStatus.PENDING)
                .build();
    }
}
