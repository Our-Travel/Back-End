package com.example.ot.app.travelInfo.service;

import com.example.ot.app.travelInfo.dto.response.ShowMapDataResponse;
import com.example.ot.app.travelInfo.entity.TravelInfo;
import com.example.ot.app.travelInfo.repository.TravelInfoRepository;
import com.example.ot.base.api.service.KakaoCategorySearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TravelInfoService {

    private final KakaoCategorySearchService kakaoCategorySearchService;
    private final TravelInfoRepository travelInfoRepository;

    public List<ShowMapDataResponse> getMapData() {
        List<TravelInfo> travelInfoList = travelInfoRepository.findAll();
        List<ShowMapDataResponse> showMapDataResponseList = new ArrayList<>();
        for(TravelInfo travelInfo: travelInfoList){
            ShowMapDataResponse data = ShowMapDataResponse.of(travelInfo);
            showMapDataResponseList.add(data);
        }
        return showMapDataResponseList;
    }
}
