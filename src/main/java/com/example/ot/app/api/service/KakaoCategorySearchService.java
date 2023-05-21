package com.example.ot.app.api.service;

import com.example.ot.app.api.dto.KakaoApiResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class KakaoCategorySearchService {

    private final RestTemplate restTemplate;
    private final KakaoUriBuilderService kakaoUriBuilderService;

    @Value("${kakao.rest.api.key}")
    private String kakaoRestApiKey;

    public KakaoApiResponseDTO requestCategorySearch(String categoryGroupCode, double longitude, double latitude){

        if(ObjectUtils.isEmpty(categoryGroupCode) || ObjectUtils.isEmpty(longitude) || ObjectUtils.isEmpty(latitude)) return null;

        URI uri = kakaoUriBuilderService.buildUriCategorySearch(categoryGroupCode, longitude, latitude);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "KakaoAK " + kakaoRestApiKey);
        HttpEntity httpEntity = new HttpEntity<>(headers);

        // kakao api 호출
        return restTemplate.exchange(uri, HttpMethod.GET, httpEntity, KakaoApiResponseDTO.class).getBody();
    }

}
