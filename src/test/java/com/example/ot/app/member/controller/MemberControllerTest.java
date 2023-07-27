package com.example.ot.app.member.controller;

import com.example.ot.app.member.dto.request.SignUpRequest;
import com.example.ot.app.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class MemberControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("회원가입 요청이 성공적으로 처리되어야 한다")
    void shouldSignUpSuccessfully() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        post("/members/signup")
                                .content("""
                                        {
                                            "username": "user2222@example.com",
                                            "password": "@a2123456",
                                            "nick_name": "user2222"
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
    @DisplayName("회원가입 요청시 필요한 정보가 누락되면 실패해야 한다")
    void shouldFailSignUpWithMissingValues() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        post("/members/signup")
                                .content("""
                                        {
                                            "username": "",
                                            "password": "1234",
                                            "nick_name": "user1"
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
                        post("/members/signup")
                                .content("""
                                        {
                                            "username": "user1@naver.com",
                                            "password": "1234",
                                            "nick_name": ""
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
    @DisplayName("아이디 중복 체크 요청이 성공적으로 처리되어야 한다")
    void shouldPassUsernameCheck() throws Exception {
        // Given
        String username = "user222@example.com";

        // When
        ResultActions resultActions = mvc
                .perform(
                        get("/members/exists/username/{username}", username)
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("아이디가 중복되었을 때 중복 체크 요청이 실패해야 한다")
    void shouldFailOnDuplicatedUsername() throws Exception {
        // Given
        String username = "user1@example.com";

        // When
        ResultActions resultActions = mvc
                .perform(
                        get("/members/exists/username/{username}", username)
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("닉네임 중복 체크 요청이 성공적으로 처리되어야 한다")
    void shouldPassNickNameCheck() throws Exception {
        // Given
        String nickName = "user3";

        // When
        ResultActions resultActions = mvc
                .perform(
                        get("/members/exists/nickName/{nickName}", nickName)
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("닉네임이 중복되었을 때 중복 체크 요청이 실패해야 한다")
    void shouldFailNickCheck() throws Exception {
        // Given
        String nickName = "user123";

        // When
        ResultActions resultActions = mvc
                .perform(
                        get("/members/exists/nickName/{nickName}", nickName)
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("회원가입 요청에서 유효성 검증을 통과하지 못했을 때 가입이 실패해야 한다")
    void shouldFailSignup() throws Exception {
        // When
        // 아이디 유효성 검증
        ResultActions resultActions = mvc
                .perform(
                        post("/members/signup")
                                .content("""
                                        {
                                            "username": "user3",
                                            "password": "@a123456",
                                            "nick_name": "user6"
                                        }
                                        """.stripIndent())
                                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is4xxClientError());

        // When
        // 비밀번호 길이 유효성 검증
        resultActions = mvc
                .perform(
                        post("/members/signup")
                                .content("""
                                        {
                                            "username": "user3@example.com",
                                            "password": "@a123",
                                            "nick_name": "user6"
                                        }
                                        """.stripIndent())
                                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is4xxClientError());

        // When
        // 비밀번호 특문포함 유효성 검증
        resultActions = mvc
                .perform(
                        post("/members/signup")
                                .content("""
                                        {
                                            "username": "user3@example.com",
                                            "password": "1a123456",
                                            "nick_name": "user6"
                                        }
                                        """.stripIndent())
                                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is4xxClientError());

        // When
        // 비밀번호 영어포함 유효성 검증
        resultActions = mvc
                .perform(
                        post("/members/signup")
                                .content("""
                                        {
                                            "username": "user3@example.com",
                                            "password": "1@123456",
                                            "nick_name": "user6"
                                        }
                                        """.stripIndent())
                                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is4xxClientError());

        // When
        // 닉네임 길이 유효성 검증
        resultActions = mvc
                .perform(
                        post("/members/signup")
                                .content("""
                                        {
                                            "username": "user3@example.com",
                                            "password": "1@5123456",
                                            "nick_name": "user6333333"
                                        }
                                        """.stripIndent())
                                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("올바른 username과 password가 제공되었을 때 JWT 키를 성공적으로 발급해야 한다")
    void successfulLoginAndJwtIssuance() throws Exception {
        // Given
        memberService.createMember(new SignUpRequest("user55@example.com", "@a123456", "user551"));

        // When
        ResultActions resultActions = mvc
                .perform(
                        post("/members/login")
                                .content("""
                                        {
                                            "username": "user55@example.com",
                                            "password": "@a123456"
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
    @DisplayName("올바르지 않은 아이디와 비밀번호로 로그인 시도는 실패한다.")
    void loginFailureWithIncorrectUsernameAndPassword() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        post("/members/login")
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
                        post("/members/login")
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

    @Test
    @DisplayName("로그인이 되어 있으면 마이페이지에 들어갈 수 있다.")
    @WithUserDetails("user1@example.com")
    void shouldMyPageSuccessfully() throws Exception {
        ResultActions resultActions = mvc
                .perform(
                        get("/members"))
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("로그인이 되어 있지 않으면 오류발생")
    void shouldFailMyPageDueToNotSignIn() throws Exception {
        ResultActions resultActions = mvc
                .perform(
                        get("/members"))
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("로그인이 되어 있으면 프로필사진 편집 페이지에 들어갈 수 있다.")
    @WithUserDetails("user1@example.com")
    void shouldProfilePageSuccessfully() throws Exception {
        ResultActions resultActions = mvc
                .perform(
                        get("/members/profile"))
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("로그인이 되어 있지 않으면 오류발생")
    void shouldProfilePageDueToNotSignIn() throws Exception {
        ResultActions resultActions = mvc
                .perform(
                        get("/members/profile"))
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("로그인이 되어 있으면 프로필 사진 업데이트가 성공한다.")
    @WithUserDetails("user1@example.com")
    void updateProfileSuccessfully() throws Exception {
        // Given
        MockMultipartFile file = new MockMultipartFile("images", "filename.jpg", "text/plain", "some image".getBytes());

        // When
        ResultActions resultActions = mvc
                .perform(
                        multipart("/members/profile-image")
                                .file(file)
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("로그인이 되어 있지만 프로필 사진을 업로드하지 않으면 업데이트는 실패한다.")
    @WithUserDetails("user1@example.com")
    void updateProfileFailureDueToNoImage() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        multipart("/members/profile")
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("로그인이 되어 있지 않으면 프로필 사진 업데이트는 실패한다.")
    void updateProfileFailureDueToNotSignIn() throws Exception {
        // Given
        MockMultipartFile file = new MockMultipartFile("images", "filename.jpg", "text/plain", "some image".getBytes());

        // When
        ResultActions resultActions = mvc
                .perform(
                        multipart("/members/profile")
                                .file(file)
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is4xxClientError());
    }
}
