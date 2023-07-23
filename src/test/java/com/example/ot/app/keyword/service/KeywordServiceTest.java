package com.example.ot.app.keyword.service;

import com.example.ot.app.host.dto.request.WriteHostInfoRequest;
import com.example.ot.app.keyword.entity.Keyword;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class KeywordServiceTest {

    @Autowired
    private KeywordService keywordService;

    @Test
    @DisplayName("키워드 저장 성공")
    void shouldSaveKeywordSuccessfully() throws Exception {
        // Given
        String keyword = "여행";

        // When
        Keyword keywordObject = keywordService.saveKeyword(keyword);

        // Then
        assertThat(keywordObject).isNotNull();
    }

}
