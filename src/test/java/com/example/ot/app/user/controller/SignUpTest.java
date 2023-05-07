package com.example.ot.app.user.controller;

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
    @DisplayName("POST 회원가입")
    void t1() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        post("/api/user/signup")
                                .content("""
                                        {
                                            "email": "user1@naver.com",
                                            "password": "1234",
                                            "nickName": "user1",
                                            "regionLevel1": "서울시",
                                            "regionLevel2": "관악구"
                                        }
                                        """.stripIndent())
                                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(handler().handlerType(SignUpController.class))
                .andExpect(handler().methodName("signUp"));
    }

}
