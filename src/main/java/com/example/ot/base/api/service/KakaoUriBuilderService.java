package com.example.ot.base.api.service;

import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class KakaoUriBuilderService {

    private static final String KAKAO_SEARCH_PLACES_BY_CATEGORY_URL = "https://dapi.kakao.com/v2/local/search/category.json";

    public URI buildUriCategorySearch(String category, double longitude, double latitude){

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(KAKAO_SEARCH_PLACES_BY_CATEGORY_URL);
        uriBuilder.queryParam("category_group_code", category);
        uriBuilder.queryParam("x", longitude);
        uriBuilder.queryParam("y", latitude);
        uriBuilder.queryParam("radius", 10000);
        uriBuilder.queryParam("size", 10);
        uriBuilder.queryParam("sort", "distance");

        URI uri = uriBuilder.build().encode().toUri();
        return uri;
    }

}
