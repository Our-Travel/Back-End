package com.example.ot.app.member.repository;

import com.example.ot.app.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByUsername(String username);
    boolean existsByNickName(String nickName);

    Optional<Member> findByUsername(String username);

    @Query("SELECT m, p.fullPath FROM Member m LEFT JOIN m.profileImage p WHERE m.id = :memberId")
    Optional<Member> findMemberWithProfileImage(@Param("memberId") Long memberId);
}
