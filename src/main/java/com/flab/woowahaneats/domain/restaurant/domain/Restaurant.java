package com.flab.woowahaneats.domain.restaurant.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flab.woowahaneats.domain.common.vo.Address;
import com.flab.woowahaneats.domain.common.vo.Location;
import com.flab.woowahaneats.domain.owner.domain.Owner;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private Owner owner;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Embedded
    @AttributeOverride(name = "detail", column = @Column(name = "address_detail"))
    private Address address;

    @Embedded
    private Location location;

    @JsonIgnore
    @Column(columnDefinition = "geography(Point, 4326)")
    private Point point;

    @Column(nullable = false)
    private double avgRating;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status", nullable = false)
    private RestaurantApprovalStatus approvalStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RestaurantCategory category;

    private static final GeometryFactory GEOMETRY_FACTORY =
            new GeometryFactory(new PrecisionModel(), 4326);

    public static Restaurant create(
            Owner owner,
            String name,
            String description,
            Address address,
            Location location,
            RestaurantCategory category
    ) {
        Point point = GEOMETRY_FACTORY.createPoint(
                new Coordinate(location.longitude(), location.latitude()));

        return Restaurant.builder()
                .owner(owner)
                .name(name)
                .description(description)
                .address(address)
                .location(location)
                .point(point)
                .avgRating(0.0)
                .approvalStatus(RestaurantApprovalStatus.PENDING)
                .category(category)
                .build();
    }
}
