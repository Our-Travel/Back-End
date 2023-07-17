package com.example.ot.app.member.repository;

import com.example.ot.app.member.entity.Member;
import com.example.ot.app.member.entity.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {
    Optional<ProfileImage> findByMember(Member member);
    Optional<ProfileImage> findByMemberId(Long Id);
}
