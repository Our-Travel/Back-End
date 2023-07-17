package com.example.ot.app.localplaces.service;

import com.example.ot.app.api.dto.DocumentDTO;
import com.example.ot.app.api.dto.KakaoApiResponseDTO;
import com.example.ot.app.api.service.KakaoCategorySearchService;
import com.example.ot.app.localplaces.entity.Hotel;
import com.example.ot.app.localplaces.entity.Spot;
import com.example.ot.app.localplaces.repository.HotelRepository;
import com.example.ot.app.localplaces.repository.SpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LocalPlacesService {

    private final KakaoCategorySearchService kakaoCategorySearchService;
    private final SpotRepository spotRepository;
    private final HotelRepository hotelRepository;

    // 관광지or숙박 정보 제공 및 DB저장.
    @Transactional
    public KakaoApiResponseDTO response(String category, double longitude, double latitude){
        KakaoApiResponseDTO apiResponseDTO = kakaoCategorySearchService.requestCategorySearch(category, latitude, longitude);
        if(category.equals("AT4")){
            saveSpotInfo(apiResponseDTO);
        }
        else if(category.equals("AD5")){
            saveHotelInfo(apiResponseDTO);
        }

        return kakaoCategorySearchService.requestCategorySearch(category, latitude, longitude);
    }

    private void saveHotelInfo(KakaoApiResponseDTO apiResponseDTO) {
        for(DocumentDTO documentDTO : apiResponseDTO.getDocumentDTOList()){
            if(hotelRepository.existsById(documentDTO.getId())) continue;
            Hotel hotel = Hotel.builder()
                    .id(documentDTO.getId())
                    .addressName(documentDTO.getAddressName())
                    .placeName(documentDTO.getPlaceName())
                    .placeUrl(documentDTO.getPlaceUrl())
                    .phone(documentDTO.getPhone())
                    .roadAddressName(documentDTO.getRoadAddressName())
                    .longitude(documentDTO.getLongitude())
                    .latitude(documentDTO.getLatitude())
                    .build();
            hotelRepository.save(hotel);
        }
    }

    private void saveSpotInfo(KakaoApiResponseDTO apiResponseDTO) {
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
    }

    public Spot findSpotById(Long siteId) {
        return spotRepository.findById(siteId).orElse(null);
    }

    public Hotel findHotelById(Long hotelId){
        return hotelRepository.findById(hotelId).orElse(null);
    }

    public boolean canResponseSpot(double latitude, double longitude) {
        if(ObjectUtils.isEmpty(latitude) || ObjectUtils.isEmpty(longitude)){
            return true;
        }
        return false;
    }

}
