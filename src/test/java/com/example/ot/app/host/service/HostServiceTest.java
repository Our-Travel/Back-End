package com.example.ot.app.host.service;

import com.example.ot.app.host.dto.request.WriteHostInfoRequest;
import com.example.ot.app.host.dto.response.HostInfoListResponse;
import com.example.ot.app.host.exception.HostException;
import com.example.ot.app.member.exception.MemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class HostServiceTest {

    @Autowired
    private HostService hostService;

    @Test
    @DisplayName("호스트 등록 성공.")
    void shouldCreateHostSuccessfully() throws Exception {
        // Given
        WriteHostInfoRequest writeHostInfoRequest = new WriteHostInfoRequest("안녕하세요.", "#여행", 1234);

        // Then
        assertDoesNotThrow(() -> hostService.createHost(writeHostInfoRequest, 2L));
    }

    @Test
    @DisplayName("존재하지 않는 MemberId이면 호스트 등록 실패.")
    void shouldCreateHostFailDueToNotExistsMemberId() throws Exception {
        // Given
        WriteHostInfoRequest writeHostInfoRequest = new WriteHostInfoRequest("안녕하세요.", "#여행", 1234);

        // Then
        assertThrows(MemberException.class, () -> hostService.createHost(writeHostInfoRequest, 80L));
    }

    @Test
    @DisplayName("Host 권한을 가진 Member는 Host정보를 얻을 수 있습니다.")
    void shouldGetHostInfoSuccessfully() throws Exception {
        // Given
        Long MemberId = 1L;

        // Then
        assertDoesNotThrow(() -> hostService.getHostInfo(MemberId));
    }

    @Test
    @DisplayName("존재하지 않는 MemberId이면 호스트 정보를 얻지 못합니다.")
    void shouldGetHostInfoFailDueToNotExistsMemberId() throws Exception {
        // Given
        Long MemberId = 80L;

        // Then
        assertThrows(HostException.class, () -> hostService.getHostInfo(MemberId));
    }

    @Test
    @DisplayName("호스트 권한이 없다면 호스트 정보를 얻지 못합니다.")
    void shouldGetHostInfoFailDueToNotExistsHostAuthorize() throws Exception {
        // Given
        Long MemberId = 2L;

        // Then
        assertThrows(HostException.class, () -> hostService.getHostInfo(MemberId));
    }

    @Test
    @DisplayName("호스트 정보 업데이트 성공.")
    void shouldUpdateHostInfoSuccessfully() throws Exception {
        // Given
        WriteHostInfoRequest writeHostInfoRequest = new WriteHostInfoRequest("안녕하세요.", "#여행", 1234);

        // Then
        assertDoesNotThrow(() -> hostService.updateHostInfo(writeHostInfoRequest, 1L));
    }

    @Test
    @DisplayName("존재하지 않는 MemberId이면 호스트 정보 업데이트 실패.")
    void shouldUpdateHostInfoFailDueToNotExistsMemberId() throws Exception {
        // Given
        WriteHostInfoRequest writeHostInfoRequest = new WriteHostInfoRequest("안녕하세요.", "#여행", 1234);

        // Then
        assertThrows(HostException.class, () -> hostService.updateHostInfo(writeHostInfoRequest, 80L));
    }

    @Test
    @DisplayName("호스트 권한이 없다면 호스트 정보 업데이트 실패.")
    void shouldUpdateHostInfoFailDueToNotExistsHostAuthorize() throws Exception {
        // Given
        WriteHostInfoRequest writeHostInfoRequest = new WriteHostInfoRequest("안녕하세요.", "#여행", 1234);

        // Then
        assertThrows(HostException.class, () -> hostService.updateHostInfo(writeHostInfoRequest, 2L));
    }

    @Test
    @DisplayName("호스트 권한 삭제 성공.")
    void shouldRemoveHostAuthorizeSuccessfully() throws Exception {
        // Given
        Long MemberId = 1L;

        // Then
        assertDoesNotThrow(() -> hostService.removeHostAuthorize(MemberId));
    }

    @Test
    @DisplayName("존재하지 않는 MemberId이면 호스트 권한 삭제 실패.")
    void shouldRemoveHostAuthorizeFailDueToNotExistsMemberId() throws Exception {
        // Given
        Long MemberId = 80L;

        // Then
        assertThrows(HostException.class, () -> hostService.removeHostAuthorize(MemberId));
    }

    @Test
    @DisplayName("호스트 권한이 없다면 호스트 권한 삭제 실패.")
    void shouldRemoveHostAuthorizeFailDueToNotExistsHostAuthority() throws Exception {
        // Given
        Long MemberId = 2L;

        // Then
        assertThrows(HostException.class, () -> hostService.removeHostAuthorize(MemberId));
    }

    @Test
    @DisplayName("해당 지역에 호스트가 있다면 지역기반의 호스트들을 불러옵니다.")
    void shouldGetHostListByRegionSuccessfully() throws Exception {
        // Given
        Integer regionCode = 11020;

        // When
        List<HostInfoListResponse> hostInfoListResponseList = hostService.getHostListByRegion(regionCode);

        // Then
        assertThat(hostInfoListResponseList).isNotNull();
    }

    @Test
    @DisplayName("해당 지역에 호스트가 없다면 지역기반의 호스트들을 불러오지 못합니다.")
    void shouldGetHostListByRegionFailDueToNotExistsHostByRegion() throws Exception {
        // Given
        Integer regionCode = 11333;

        // Then
        assertThrows(HostException.class, () -> hostService.getHostListByRegion(regionCode));
    }

}
