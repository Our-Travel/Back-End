package com.example.ot.app.mypage.controller;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class MyPageTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("GET 마이페이지 초기화면 로그인이 되어있다면 성공")
    @WithUserDetails("user1@example.com")
    void t1() throws Exception {

        // When
        ResultActions resultActions = mvc
                .perform(
                        get("/api/mypage")
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("GET 마이페이지 초기화면 로그인이 되어있지 않다면 실패")
    void t2() throws Exception {

        // When
        ResultActions resultActions = mvc
                .perform(
                        get("/api/mypage")
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is4xxClientError());
    }

}
