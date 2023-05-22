package com.example.ot.app.localplaces.controller;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class LocalPlacesControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("GET 주변관광지 위도와 경도 모두 있으면 성공")
    @WithUserDetails("user1@example.com")
    void t1() throws Exception {

        // When
        ResultActions resultActions = mvc
                .perform(
                        get("/api/local-place/spot?latitude=37.51766013568054&longitude=126.95803386590158")
                )
                .andDo(print());

        // Then

        resultActions
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("GET 주변관광지 위도와 경도 하나라도 없으면 실패")
    @WithUserDetails("user1@example.com")
    void t2() throws Exception {

        // When
        ResultActions resultActions = mvc
                .perform(
                        get("/api/local-place/spot?latitude=37.51766013568054")
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is4xxClientError());

        // When
        mvc
                .perform(
                        get("/api/local-place/spot?longitude=126.95803386590158")
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is4xxClientError());

    }

    @Test
    @DisplayName("GET 하나의 관광지 세부정보 제공.")
    @WithUserDetails("user1@example.com")
    void t3() throws Exception {

        // When
        ResultActions resultActions = mvc
                .perform(
                        get("/api/local-place/spot?latitude=37.51766013568054&longitude=126.95803386590158")
                )
                .andDo(print());

        // Then

        resultActions
                .andExpect(status().is2xxSuccessful());

        // When
        mvc
                .perform(
                        get("/api/local-place/spot?8252248")
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is2xxSuccessful());
    }
}
