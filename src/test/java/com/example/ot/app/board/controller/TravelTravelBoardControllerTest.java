package com.example.ot.app.board.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class TravelTravelBoardControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("동행 게시판 생성")
    @WithUserDetails("user1@example.com")
    void shouldCreateBoardSuccessfully() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        post("/api/boards")
                                .content("""
                                    {
                                        "title": "제목입니다",
                                        "content": "내용입니다",
                                        "region_code": 123,
                                        "number_of_travelers": 3,
                                        "recruitment_period_start": "2030-08-01",
                                        "recruitment_period_end": "2030-08-03",
                                        "journey_period_start": "2030-08-04",
                                        "journey_period_end": "2030-08-08"
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
    @DisplayName("로그인을 하지 않으면 게시판 생성을 못한다.")
    void shouldFailCreateBoardWithoutLogin() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        post("/api/boards")
                                .content("""
                                    {
                                        "title": "제목입니다",
                                        "content": "내용입니다",
                                        "region_code": 123,
                                        "number_of_travelers": 3,
                                        "recruitment_period_start": "2030-08-01",
                                        "recruitment_period_end": "2030-08-03",
                                        "journey_period_start": "2030-08-04",
                                        "journey_period_end": "2030-08-08"
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
    @DisplayName("모집기간 시작일은 현재 날짜 이후이어야 합니다.")
    @WithUserDetails("user1@example.com")
    void shouldFailIfRecruitmentStartsInPast() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        post("/api/boards")
                                .content("""
                                    {
                                        "title": "제목입니다",
                                        "content": "내용입니다",
                                        "region_code": 123,
                                        "number_of_travelers": 3,
                                        "recruitment_period_start": "2020-08-01",
                                        "recruitment_period_end": "2030-08-03",
                                        "journey_period_start": "2030-08-04",
                                        "journey_period_end": "2030-08-08"
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
    @DisplayName("모집기간 종료일이 시작일보다 빠르게 설정할 수 없습니다.")
    @WithUserDetails("user1@example.com")
    void shouldFailIfRecruitmentEndsBeforeStart() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        post("/api/boards")
                                .content("""
                                    {
                                        "title": "제목입니다",
                                        "content": "내용입니다",
                                        "region_code": 123,
                                        "number_of_travelers": 3,
                                        "recruitment_period_start": "2030-08-05",
                                        "recruitment_period_end": "2030-08-03",
                                        "journey_period_start": "2030-08-04",
                                        "journey_period_end": "2030-08-08"
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
    @DisplayName("여행기간 종료일이 시작일보다 빠르게 설정할 수 없습니다.")
    @WithUserDetails("user1@example.com")
    void shouldFailIfJourneyEndsBeforeStart() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        post("/api/boards")
                                .content("""
                                    {
                                        "title": "제목입니다",
                                        "content": "내용입니다",
                                        "region_code": 123,
                                        "number_of_travelers": 3,
                                        "recruitment_period_start": "2030-08-01",
                                        "recruitment_period_end": "2030-08-03",
                                        "journey_period_start": "2030-08-04",
                                        "journey_period_end": "2030-08-02"
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
    @DisplayName("여행기간 종료일이 시작일보다 빠르게 설정할 수 없습니다.")
    @WithUserDetails("user1@example.com")
    void shouldFailIfJourneyStartsBeforeRecruitmentEnds() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        post("/api/boards")
                                .content("""
                                    {
                                        "title": "제목입니다",
                                        "content": "내용입니다",
                                        "region_code": 123,
                                        "number_of_travelers": 3,
                                        "recruitment_period_start": "2030-08-01",
                                        "recruitment_period_end": "2030-08-03",
                                        "journey_period_start": "2030-08-06",
                                        "journey_period_end": "2030-08-03"
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
    @DisplayName("동행 게시판 조회")
    @WithUserDetails("user1@example.com")
    void shouldShowBoardSuccessfully() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        get("/api/boards/{boardId}", 1)
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("로그인을 하지 않으면 게시판 조회 실패")
    void shouldShowBoardFailWithoutLogin() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        get("/api/boards/{boardId}", 1)
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("존재하지 않는 게시판은 조회할 수 없다.")
    @WithUserDetails("user1@example.com")
    void shouldShowBoardFailDueToNotExistsBoard() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        get("/api/boards/{boardId}", 100)
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("작성자가 동행 게시판 조회하면 board-writer는 true이다.")
    @WithUserDetails("user1@example.com")
    void shouldShowBoardSuccessfullyWhenWriter() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        get("/api/boards/{boardId}", 1)
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data.board_writer").value(true));
    }

    @Test
    @DisplayName("작성자가 아닌 유저가 동행 게시판 조회하면 board-writer는 false이다.")
    @WithUserDetails("user2@example.com")
    void shouldShowBoardSuccessfullyWhenDoesNotWriter() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        get("/api/boards/{boardId}", 1)
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data.board_writer").value(false));
    }

    @Test
    @DisplayName("좋아요를 누르지 않았다면, 동행 특정 게시판 좋아요 성공.")
    @WithUserDetails("user1@example.com")
    void shouldLikeBoardSuccessfully() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        post("/api/boards/{boardId}", 1)
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.msg").value("'좋아요'를 눌렀습니다."));
    }

    @Test
    @DisplayName("좋아요를 눌렀었다면, 동행 특정 게시판 좋아요 취소 성공.")
    @WithUserDetails("user2@example.com")
    void shouldLikeBoardSuccessfullyWhenCancelLikeBoard() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        post("/api/boards/{boardId}", 1)
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.msg").value("'좋아요'를 취소했습니다."));
    }

    @Test
    @DisplayName("로그인을 하지 않으면 동행 특정 게시판 좋아요 실패.")
    void shouldLikeBoardFailWithoutLogin() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        post("/api/boards/{boardId}", 1)
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("동행 게시판 수정 페이지 조회")
    @WithUserDetails("user1@example.com")
    void shouldEditBoardPageSuccessfully() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        get("/api/boards/edit/{boardId}", 1)
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("로그인을 하지 않으면 동행 게시판 수정 페이지 조회 실패")
    @WithUserDetails("user2@example.com")
    void shouldEditBoardPageFailWithoutLogin() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        get("/api/boards/edit/{boardId}", 1)
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("게시글 작성자가 아니라서 동행 게시판 수정 페이지 조회 실패")
    @WithUserDetails("user2@example.com")
    void shouldEditBoardPageFailDueToUnauthorized() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        get("/api/boards/edit/{boardId}", 1)
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("존재하지 않는 게시판은 수정 페이지를 조회할 수 없다.")
    @WithUserDetails("user2@example.com")
    void shouldEditBoardPageFailDueToNotExistsBoard() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        get("/api/boards/edit/{boardId}", 1000)
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is4xxClientError());
    }
}
