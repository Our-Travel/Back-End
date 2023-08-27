package com.example.ot.app.travelInfo.repository;

import com.example.ot.app.travelInfo.entity.LikedTravelInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikedTravelInfoRepository extends JpaRepository<LikedTravelInfo, Long> {
}
