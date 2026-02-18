package com.flab.woowahaneats.domain.member.repository;

import com.flab.woowahaneats.domain.member.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class MemberRepositoryTest {

    MemberRepository memberRepository = new MemberRepository();

    Member member = new Member(
            1L,
            "홍길동",
            "123-45-67890",
            "국민은행",
            "123456-01-123456",
            "password123!",
            "hong@example.com",
            "010-1234-5678",
            "서울특별시 강남구 테헤란로 123"
    );

    @Test
    void save(){
        memberRepository.save(member);

        Member findMember = memberRepository.findById(member.getId());

        Assertions.assertThat(findMember).isEqualTo(member);
    }

}
