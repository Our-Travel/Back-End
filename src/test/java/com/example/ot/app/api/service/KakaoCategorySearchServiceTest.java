package com.example.ot.app.api.service;

import com.example.ot.app.api.dto.KakaoApiResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class KakaoCategorySearchServiceTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private KakaoCategorySearchService kakaoCategorySearchService;

    @Test
    @DisplayName("모든값이 있다면 document 정상적으로 반환")
    void t1() throws Exception {
        // Given
        String categoryGroupCode = "AT4";
        double longitude = 127.037033003036;
        double latitude = 37.5960650456809;

        // When
        KakaoApiResponseDTO kakaoApiResponseDTO = kakaoCategorySearchService.requestCategorySearch(categoryGroupCode, longitude, latitude);

        // Then
        assertThat(kakaoApiResponseDTO.getMetaDTO().getTotalCount()).isNotNull();
        assertThat(kakaoApiResponseDTO.getDocumentDTOList().size()).isNotNull();
    }

    @Test
    @DisplayName("하나라도 값을 안넣으면 null 반환")
    void t2() throws Exception {
        // Given
        String categoryGroupCode = null;
        double longitude = 127.037033003036;
        double latitude = 37.5960650456809;

        // When
        KakaoApiResponseDTO kakaoApiResponseDTO = kakaoCategorySearchService.requestCategorySearch(categoryGroupCode, longitude, latitude);

        // Then
        assertThat(kakaoApiResponseDTO).isNull();
    }
}
