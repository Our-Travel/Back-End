package com.example.ot.app.travelInfo.repository;

import com.example.ot.app.travelInfo.entity.LikedTravelInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LikedTravelInfoRepository extends JpaRepository<LikedTravelInfo, Long> {

    @Query("select l from LikedTravelInfo l join l.member m join l.travelInfo t " +
            "where m.id = :memberId and t.contentId = :contentId")
    Optional<LikedTravelInfo> findByContentIdAndMember(@Param("contentId")int contentId, @Param("memberId")Long memberId);
}
