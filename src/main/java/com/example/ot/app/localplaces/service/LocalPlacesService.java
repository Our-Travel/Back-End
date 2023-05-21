package com.example.ot.app.localplaces.service;

import com.example.ot.app.api.dto.KakaoApiResponseDTO;
import com.example.ot.app.api.service.KakaoCategorySearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LocalPlacesService {

    private final KakaoCategorySearchService kakaoCategorySearchService;

    public List<KakaoApiResponseDTO> response(String category, double latitude, double longitude){
        return (List<KakaoApiResponseDTO>) kakaoCategorySearchService.requestCategorySearch(category, latitude, longitude);
    }
}
