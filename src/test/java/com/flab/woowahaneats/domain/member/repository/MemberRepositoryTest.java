package com.flab.woowahaneats.domain.member.repository;

import com.flab.woowahaneats.domain.common.vo.Address;
import com.flab.woowahaneats.domain.common.vo.BankAccount;
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
            .address(new Address("서울특별시", "강남구", "테헤란로", "", "123", 37.5665, 126.9780))
            .businessNotificationCertUrl("aklsdfjkalsjdfl")
            .businessRegistrationCertUrl("aklsdfjkalsjdfl")
            .bankAccount(new BankAccount("국민은행", "123456-01-123456"))
            .build();

    @Test
    void save(){
        ownerRepository.save(owner);

        Owner findOwner = ownerRepository.findById(owner.getId());

        Assertions.assertThat(findOwner).isEqualTo(owner);
    }

}
