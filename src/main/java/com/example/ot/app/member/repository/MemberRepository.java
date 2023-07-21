package com.example.ot.app.member.repository;

import com.example.ot.app.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByUsername(String username);
    boolean existsByNickName(String nickName);

    Optional<Member> findByUsername(String username);
}
