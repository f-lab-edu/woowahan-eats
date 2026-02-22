package com.flab.woowahaneats.domain.member.repository;

import com.flab.woowahaneats.domain.member.domain.Owner;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class MemberRepositoryTest {

    OwnerRepository ownerRepository = new OwnerRepositoryImpl();

    Owner owner = Owner.builder()
            .id(1L)
            .name("홍길동")
            .password("password123!")
            .email("hong@example.com")
            .phoneNumber("010-1234-5678")
            .address("서울특별시 강남구 테헤란로 123")
            .businessNotificationCertUrl("aklsdfjkalsjdfl")
            .businessRegistrationCertUrl("aklsdfjkalsjdfl")
            .accountNumber("123456-01-123456")
            .bankName("국민은행")
            .build();

    @Test
    void save(){
        ownerRepository.save(owner);

        Owner findOwner = ownerRepository.findById(owner.getId());

        Assertions.assertThat(findOwner).isEqualTo(owner);
    }

}
