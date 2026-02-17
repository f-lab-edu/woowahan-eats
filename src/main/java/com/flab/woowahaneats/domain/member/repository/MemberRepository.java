package com.flab.woowahaneats.domain.member.repository;

import com.flab.woowahaneats.domain.member.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class MemberRepository {
    HashMap<Long, Member> memberRepository = new HashMap<>();

    public void save(Member member) {
        memberRepository.put(member.getId(), member);
    }
    public Member findById(Long id) {
        return memberRepository.get(id);
    }
}
