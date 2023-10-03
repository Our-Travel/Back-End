package com.example.ot.app.member.service;

import com.example.ot.app.member.entity.Member;
import com.example.ot.app.member.exception.MemberException;
import com.example.ot.app.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.ot.app.member.exception.ErrorCode.MEMBER_NOT_EXISTS;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberRedisService {

    private final MemberRepository memberRepository;

    @Cacheable(value = "members", key = "#memberId")
    public Member findByMemberId(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_EXISTS));
    }
}
