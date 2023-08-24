package com.example.ot.app.member.repository;

import com.example.ot.app.member.entity.Member;
import com.example.ot.app.member.exception.MemberException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import static com.example.ot.app.member.exception.ErrorCode.MEMBER_NOT_EXISTS;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByUsername(String username);
    boolean existsByNickName(String nickName);

    default Member findByMemberId(Long memberId){
        return findById(memberId).orElseThrow(() -> new MemberException(MEMBER_NOT_EXISTS));
    }

    Optional<Member> findByUsername(String username);
}
