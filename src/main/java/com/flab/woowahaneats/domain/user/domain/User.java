package com.flab.woowahaneats.domain.user.domain;

import com.flab.woowahaneats.domain.common.vo.Address;
import com.flab.woowahaneats.domain.common.vo.Location;
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

    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "province", column = @Column(name = "province")),
            @AttributeOverride(name = "city", column = @Column(name = "city")),
            @AttributeOverride(name = "district", column = @Column(name = "district")),
            @AttributeOverride(name = "village", column = @Column(name = "village")),
            @AttributeOverride(name = "detail", column = @Column(name = "address_detail"))
    })
    private Address address;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "longitude"))
    })
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
            Long accountId,
            String name,
            String phoneNumber,
            Address address,
            Location location,
            String profileImageUrl,
            String nickName
    ) {
        return User.builder()
                .accountId(accountId)
                .name(name)
                .phoneNumber(phoneNumber)
                .address(address)
                .location(location)
                .profileImageUrl(profileImageUrl)
                .nickName(nickName)
                .build();
    }
}
