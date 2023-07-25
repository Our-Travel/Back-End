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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class TravelBoardControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("게시판 생성")
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
    void shouldFailWithoutLogin() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        post("/api/boards")
                                .content("""
                                    {
                                        "title": "제목 입니다.",
                                        "content": "내용 입니다."
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
    @DisplayName("제목과 내용을 작성하지 않으면 게시판 생성을 못한다.")
    void shouldFailWithoutTitleOrContent() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        post("/api/boards")
                                .content("""
                                    {
                                        "title": "제목 입니다."
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
                        post("/api/boards")
                                .content("""
                                    {
                                        "content": "내용 입니다."
                                    }
                                    """)
                                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is4xxClientError());
    }
}
