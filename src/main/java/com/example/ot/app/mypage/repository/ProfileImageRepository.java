package com.example.ot.app.mypage.repository;

import com.example.ot.app.mypage.entity.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {
}
