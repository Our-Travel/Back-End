package com.example.ot.app.localplaces.service;

import com.example.ot.app.api.dto.DocumentDTO;
import com.example.ot.app.api.dto.KakaoApiResponseDTO;
import com.example.ot.app.api.service.KakaoCategorySearchService;
import com.example.ot.app.localplaces.entity.Spot;
import com.example.ot.app.localplaces.repository.SpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LocalPlacesService {

    private final KakaoCategorySearchService kakaoCategorySearchService;
    private final SpotRepository spotRepository;

    // 관광지 정보 제공 및 DB저장.
    @Transactional
    public KakaoApiResponseDTO response(String category, double latitude, double longitude){
        KakaoApiResponseDTO apiResponseDTO = kakaoCategorySearchService.requestCategorySearch(category, latitude, longitude);
        for(DocumentDTO documentDTO : apiResponseDTO.getDocumentDTOList()){
            if(spotRepository.existsById(documentDTO.getId())) continue;
            Spot spot = Spot.builder()
                    .id(documentDTO.getId())
                    .addressName(documentDTO.getAddressName())
                    .placeName(documentDTO.getPlaceName())
                    .placeUrl(documentDTO.getPlaceUrl())
                    .phone(documentDTO.getPhone())
                    .roadAddressName(documentDTO.getRoadAddressName())
                    .longitude(documentDTO.getLongitude())
                    .latitude(documentDTO.getLatitude())
                    .build();
            spotRepository.save(spot);
        }

        return kakaoCategorySearchService.requestCategorySearch(category, latitude, longitude);
    }
}
