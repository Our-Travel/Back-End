package com.example.ot.app.travelInfo.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class TravelInfoControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("관광지 데이터를 보여준다.")
    @WithUserDetails("user1@example.com")
    void shouldShowMapDataSuccessfully() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        get("/local-place?contentTypeId=15")
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("로그인을 하지 않으면 관광지 데이터를 볼수없다.")
    void shouldShowMapDataFailDueToWithoutLogin() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        get("/local-place")
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("하나의 관광지 세부정보를 보여준다.")
    @WithUserDetails("user1@example.com")
    void shouldShowOneMapDataSuccessfully() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        get("/local-place/{contentId}", 1000)
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("존재하지 않는 관광지를 요청하면 실패.")
    @WithUserDetails("user1@example.com")
    void shouldShowOneMapDataFailDueToNotExistsData() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        get("/local-place/{contentId}", 3000)
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("로그인을 하지 않으면 관광지를 요청 실패.")
    void shouldShowOneMapDataFailDueToWithoutLogin() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        get("/local-place/{contentId}", 3000)
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("좋아요를 누르지 않았다면, 관광지 좋아요 성공.")
    @WithUserDetails("user1@example.com")
    void shouldLikeTravelInfoSuccessfully() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        post("/local-place/{contentId}", 1000)
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.msg").value("'좋아요'를 눌렀습니다."));
    }

    @Test
    @DisplayName("좋아요를 눌렀었다면, 관광지 좋아요 취소 성공.")
    @WithUserDetails("user2@example.com")
    void shouldLikeTravelInfoSuccessfullyWhenCancelLikeTravelInfo() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        post("/local-place/{contentId}", 1000)
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.msg").value("'좋아요'를 취소했습니다."));
    }

    @Test
    @DisplayName("로그인을 하지 않으면, 관광지 좋아요 실패.")
    void shouldLikeTravelInfoFailWithoutLogin() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        post("/local-place/{contentId}", 1)
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("로그인을 하지 않으면 유저의 관광지 좋아요 리스트를 가져오지 못한다.")
    @WithUserDetails("user1@example.com")
    void shouldGetLikedTravelInfoListSuccessfully() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        get("/local-place/list?contentTypeId=12")
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("로그인을 하지 않으면 유저의 관광지 좋아요 리스트를 가져오지 못한다.")
    void shouldGetLikedTravelInfoListFailDueToWithoutLogin() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        get("/local-place/list/{memberId}", 1)
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is4xxClientError());
    }
}
