package com.example.ot.app.travelInfo.repository;

import com.example.ot.app.travelInfo.entity.TravelInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TravelInfoRepository extends JpaRepository<TravelInfo, Long>, TravelInfoRepositoryCustom {


    Optional<TravelInfo> findByContentId(Integer contentId);
}
