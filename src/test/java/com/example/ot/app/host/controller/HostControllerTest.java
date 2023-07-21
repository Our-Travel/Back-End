package com.example.ot.app.host.controller;

import com.example.ot.app.keyword.repository.KeywordRepository;
import com.example.ot.app.member.entity.Member;
import com.example.ot.app.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class HostControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private KeywordRepository keywordRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("host 등록")
    @WithUserDetails("user1@example.com")
    void shouldRegisterHostSuccessfully() throws Exception {
        // When
        Member member = memberRepository.findByUsername("user1@example.com").orElse(null);

        ResultActions resultActions = mvc
                .perform(
                        post("/api/hosts")
                                .content("""
                                    {
                                        "introduction": "저는 호스트가 되고싶어요.",
                                        "hash_tag": "#호스트 #여행",
                                        "region_code": 10001
                                    }
                                    """)
                                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is2xxSuccessful());
        assertThat(member.isHostAuthority()).isTrue();
        assertThat(keywordRepository.count()).isEqualTo(2);
    }

    @Test
    @DisplayName("host 등록에서 지역코드를 입력하지 않는 경우 오류발생")
    @WithUserDetails("user1@example.com")
    void shouldFailWithoutRegion() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        post("/api/hosts")
                                .content("""
                                    {
                                        "introduction": "저는 호스트가 되고싶어요.",
                                        "hash_tag": "#호스트 #여행"
                                    }
                                    """)
                                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is4xxClientError());

    }

    @Test
    @DisplayName("host 등록에서 자기소개 2~40자 아닌 경우 오류발생")
    @WithUserDetails("user1@example.com")
    void shouldFailWithInvalidIntroLength() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        post("/api/hosts")
                                .content("""
                                    {
                                        "introduction": "아",
                                        "hash_tag": "#호스트 #여행",
                                        "region_code": 10001
                                    }
                                    """)
                                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is4xxClientError());

        // When
        resultActions = mvc
                .perform(
                        post("/api/hosts")
                                .content("""
                                    {
                                        "introduction": "이",
                                        "hash_tag": "#호스트 #여행",
                                        "region_code": 10001
                                    }
                                    """)
                                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is4xxClientError());

    }

    @Test
    @DisplayName("host 등록에서 해시태그를 입력하지 않는 경우 오류발생")
    @WithUserDetails("user1@example.com")
    void shouldFailWithoutHashTag() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        post("/api/hosts")
                                .content("""
                                    {
                                        "introduction": "저는 호스트가 되고싶어요.",
                                        "hash_tag": ""
                                        "region_code": 10001
                                    }
                                    """)
                                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is4xxClientError());

    }

    @Test
    @DisplayName("host 등록에서 로그인이 안되어 있다면 접근 불가.")
    void shouldFailWithoutLogin() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        post("/api/hosts")
                                .content("""
                                    {
                                        "introduction": "저는 호스트가 되고싶어요.",
                                        "hash_tag": "#여행"
                                        "region_code": 10001
                                    }
                                    """)
                                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is4xxClientError());

    }

    @Test
    @DisplayName("host 권한을 가지고 있다면 호스트 수정 페이지 접근 성공")
    @WithUserDetails("user1@example.com")
    void shouldIntoHostEditPageSuccessfullyDueToHavingHostAuthority() throws Exception {
        ResultActions resultActions = mvc
                .perform(
                        get("/api/hosts"))
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("host 권한을 가지고 있지 않다면 호스트 수정 페이지 접근 실패")
    @WithUserDetails("user2@example.com")
    void shouldFailDueToNotExistsHostAuthority() throws Exception {
        ResultActions resultActions = mvc
                .perform(
                        get("/api/hosts"))
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("호스트 정보 수정")
    @WithUserDetails("user1@example.com")
    void shouldEditHostInfoSuccessfully() throws Exception {
        ResultActions resultActions = mvc
                .perform(
                        patch("/api/hosts")
                                .content("""
                                    {
                                        "introduction": "저는 호스트가 되고싶습니다~~!",
                                        "hash_tag": "#호스트 #여행 #함께 #서울",
                                        "region_code": 10002
                                    }
                                    """)
                                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("호스트 수정 정보들을 입력하지 않아서 호스트 정보 수정 실패")
    @WithUserDetails("user1@example.com")
    void shouldFailEditHostInfoDueToBlank() throws Exception {
        ResultActions resultActions = mvc
                .perform(
                        patch("/api/hosts")
                                .content("""
                                    {
                                        "hash_tag": "#호스트 #여행 #함께 #서울",
                                        "region_code": 10002
                                    }
                                    """)
                                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is4xxClientError());

        resultActions = mvc
                .perform(
                        patch("/api/hosts")
                                .content("""
                                    {
                                        "introduction": "저는 호스트가 되고싶습니다~~!",
                                        "region_code": 10002
                                    }
                                    """)
                                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is4xxClientError());

        resultActions = mvc
                .perform(
                        patch("/api/hosts")
                                .content("""
                                    {
                                        "introduction": "저는 호스트가 되고싶습니다~~!",
                                        "hash_tag": "#호스트 #여행 #함께 #서울"
                                    }
                                    """)
                                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("host 권한을 가지고 있지 않다면 호스트 수정 실패")
    @WithUserDetails("user2@example.com")
    void shouldFailEditHostInfoDueToNotExistsHostAuthority() throws Exception {
        ResultActions resultActions = mvc
                .perform(
                        patch("/api/hosts")
                                .content("""
                                    {
                                        "introduction": "저는 호스트가 되고싶습니다~~!",
                                        "hash_tag": "#호스트 #여행 #함께 #서울",
                                        "region_code": 10002
                                    }
                                    """)
                                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("host 권한을 삭제 성공")
    @WithUserDetails("user1@example.com")
    void shouldRemoveHostAuthoritySuccessfully() throws Exception {
        ResultActions resultActions = mvc
                .perform(
                        delete("/api/hosts"))
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("host 권한을 가지고 있지않아서 삭제 실패")
    @WithUserDetails("user2@example.com")
    void shouldRemoveHostAuthorityFailDueToNotExistsHostAuthority() throws Exception {
        ResultActions resultActions = mvc
                .perform(
                        delete("/api/hosts"))
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("각 지역의 호스트들을 불러옵니다.")
    @WithUserDetails("user1@example.com")
    void shouldGetHostsByRegionSuccessfully() throws Exception {
        ResultActions resultActions = mvc
                .perform(
                        get("/api/hosts/list?regionCode=11020"))
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("해당지역에 호스트가 없다면 실패")
    @WithUserDetails("user2@example.com")
    void shouldGetHostsByRegionFailDueToHostsNotExists() throws Exception {
        ResultActions resultActions = mvc
                .perform(
                        get("/api/hosts/list?regionCode=11033"))
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is4xxClientError());
    }



}
