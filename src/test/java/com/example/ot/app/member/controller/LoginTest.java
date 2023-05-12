package com.example.ot.app.member.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class LoginTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("POST 올바른 username과 password를 넘기면 JWT키를 발급해준다.")
    void t1() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        post("/api/member/login")
                                .content("""
                                        {
                                            "username": "user1@example.com",
                                            "password": "1234"
                                        }
                                        """.stripIndent())
                                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is2xxSuccessful());

        MvcResult mvcResult = resultActions.andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();

        String authentication = response.getHeader("Authentication");

        assertThat(authentication).isNotEmpty();
    }

    @Test
    @DisplayName("POST 올바르지 않은 username과 password 데이터를 넘기면 실패")
    void t3() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        post("/api/member/login")
                                .content("""
                                        {
                                            "username": "",
                                            "password": "1234"
                                        }
                                        """.stripIndent())
                                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is4xxClientError());

        mvc
                .perform(
                        post("/api/member/login")
                                .content("""
                                        {
                                            "username": "user1@example.com",
                                            "password": ""
                                        }
                                        """.stripIndent())
                                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is4xxClientError());
    }

//    @Test
//    @DisplayName("로그인 후 얻은 JWT 토큰으로 로그인 한 회원의 정보를 얻을 수 있다.")
//    void t4() throws Exception {
//        // When
//        ResultActions resultActions = mvc
//                .perform(
//                        post("/api/members/login")
//                                .content("""
//                                        {
//                                            "username": "user1@example.com",
//                                            "password": "1234"
//                                        }
//                                        """.stripIndent())
//                                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
//                )
//                .andDo(print());
//
//        // Then
//        resultActions
//                .andExpect(status().is2xxSuccessful());
//
//        MvcResult mvcResult = resultActions.andReturn();
//
//        MockHttpServletResponse response = mvcResult.getResponse();
//
//        String accessToken = response.getHeader("Authentication");
//
//        resultActions = mvc
//                .perform(
//                        get("/api/main")
//                                .header("Authorization", "Bearer " + accessToken)
//                )
//                .andDo(print());
//
//        // Then
//        resultActions
//                .andExpect(status().is2xxSuccessful());
//    }
}
