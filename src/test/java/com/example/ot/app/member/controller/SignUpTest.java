package com.example.ot.app.member.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class SignUpTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("POST 회원가입 성공")
    void t1() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        post("/api/member/signup")
                                .content("""
                                        {
                                            "username": "user3@example.com",
                                            "password": "1234",
                                            "nickName": "user3"
                                        }
                                        """.stripIndent())
                                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("signUp"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("POST 회원가입에서 값 누락하여 실패")
    void t2() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        post("/api/member/signup")
                                .content("""
                                        {
                                            "username": "",
                                            "password": "1234",
                                            "nickName": "user1"
                                        }
                                        """.stripIndent())
                                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("signUp"))
                .andExpect(status().is4xxClientError());

        // When
        resultActions = mvc
                .perform(
                        post("/api/member/signup")
                                .content("""
                                        {
                                            "username": "user1@naver.com",
                                            "password": "1234",
                                            "nickName": ""
                                        }
                                        """.stripIndent())
                                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("signUp"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("GET 아이디 중복 체크 성공")
    void t3() throws Exception {
        // Given
        String username = "user4@example.com";

        // When
        ResultActions resultActions = mvc
                .perform(
                        get("/api/member/check-username/{username}", username)
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("GET 아이디 중복체크에서 중복되어서 실패")
    void t4() throws Exception {
        // Given
        String username = "user1@example.com";

        // When
        ResultActions resultActions = mvc
                .perform(
                        get("/api/member/check-username/{username}", username)
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("GET 닉네임 중복 체크 성공")
    void t5() throws Exception {
        // Given
        String nickName = "user3";

        // When
        ResultActions resultActions = mvc
                .perform(
                        get("/api/member/check-nickName/{nickName}", nickName)
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("GET 닉네임 중복체크에서 중복되어서 실패")
    void t6() throws Exception {
        // Given
        String nickName = "user1";

        // When
        ResultActions resultActions = mvc
                .perform(
                        get("/api/member/check-nickName/{nickName}", nickName)
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is4xxClientError());
    }
}
