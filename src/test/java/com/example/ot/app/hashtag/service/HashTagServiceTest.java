package com.example.ot.app.hashtag.service;

import com.example.ot.app.host.entity.Host;
import com.example.ot.app.host.repository.HostRepository;
import com.example.ot.app.host.service.HostService;
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
public class HashTagServiceTest {

    @Autowired
    private HashTagService hashTagService;

    @Autowired
    private HostRepository hostRepository;

    @Test
    @DisplayName("해시태그 저장 성공")
    void shouldApplyHashTagSuccessfully() throws Exception {
        // Given
        String hashTag = "#여행 #떠나요 #다같이 #추억";
        Host host = hostRepository.findByMemberId(1L).orElse(null);

        // Then
        assertDoesNotThrow(() -> hashTagService.applyHashTags(host, hashTag));
    }

    @Test
    @DisplayName("호스트의 해시태그를 가져옵니다.")
    void shouldGetHashTagSuccessfully() throws Exception {
        // Given
        Long hostId = 1L;

        // When
        String hashTag = hashTagService.getHashTag(hostId);

        // Then
        assertThat(hashTag).isNotNull();
    }

    @Test
    @DisplayName("호스트의 해시태그를 업데이트합니다.")
    void shouldUpdateHashTagSuccessfully() throws Exception {
        // Given
        String hashTag = "#여행 #떠나요 #다같이 #추억";
        Host host = hostRepository.findByMemberId(1L).orElse(null);

        // Then
        assertDoesNotThrow(() -> hashTagService.updateHashTag(hashTag, host));
    }

    @Test
    @DisplayName("호스트의 해시태그를 삭제합니다.")
    void shouldDeleteHashTagSuccessfully() throws Exception {
        // Given
        Long hostId = 1L;

        // Then
        assertDoesNotThrow(() -> hashTagService.deleteHashTag(hostId));
    }

}
