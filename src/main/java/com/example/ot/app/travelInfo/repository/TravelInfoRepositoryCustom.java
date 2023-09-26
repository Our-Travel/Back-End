package com.example.ot.app.travelInfo.repository;

import com.example.ot.app.travelInfo.entity.TravelInfo;

import java.util.List;

public interface TravelInfoRepositoryCustom {

    List<TravelInfo> findByContentTypeId(String contentTypeId);
}
