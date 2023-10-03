package com.example.ot.app.member.service;

import com.example.ot.app.member.dto.request.SignUpRequest;
import com.example.ot.app.member.dto.response.MyPageResponse;
import com.example.ot.app.member.entity.Member;
import com.example.ot.app.member.entity.ProfileImage;
import com.example.ot.app.member.exception.MemberException;
import com.example.ot.app.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("아이디 중복체크 성공.")
    void shouldCheckUsernameSuccessfully() throws Exception {
        // Given
        String username = "testUsername@example.com";

        // Then
        assertDoesNotThrow(() -> memberService.checkUsername(username));
    }

    @Test
    @DisplayName("중복되는 아이디가 있어서 중복체크 실패.")
    void shouldCheckUsernameFailDueToExistsUsername() throws Exception {
        // Given
        String username = "user1@example.com";

        // Then
        assertThrows(MemberException.class, () -> memberService.checkUsername(username));
    }

    @Test
    @DisplayName("닉네임 중복체크 성공.")
    void shouldCheckNickNameSuccessfully() throws Exception {
        // Given
        String nickName = "testNickName";

        // Then
        assertDoesNotThrow(() -> memberService.checkNickName(nickName));
    }

    @Test
    @DisplayName("중복되는 닉네임이 있어서 중복체크 실패.")
    void shouldCheckNickNameFailDueToExistsNickName() throws Exception {
        // Given
        String nickName = "user123";

        // Then
        assertThrows(MemberException.class, () -> memberService.checkNickName(nickName));
    }

    @Test
    @DisplayName("회원가입 성공.")
    void shouldSignUpSuccessfully() throws Exception {
        // Given
        SignUpRequest signUpRequest = new SignUpRequest("testUsername@example.com", "", "testNickName");

        // Then
        assertDoesNotThrow(() -> memberService.createMember(signUpRequest));
    }

    @Test
    @DisplayName("중복된 아이디 또는 닉네임이 있다면 회원가입 실패.")
    void shouldSignUpFailDueToExistsUsernameOrNickName() throws Exception {
        // Given
        SignUpRequest signUpRequest = new SignUpRequest("testUsername@example.com", "", "user123");

        // Then
        assertThrows(MemberException.class, () -> memberService.createMember(signUpRequest));
    }

    @Test
    @DisplayName("Member 번호를 통해 Member를 얻을 수 있습니다.")
    void shouldFindByMemberIdSuccessfully() throws Exception {
        // Given
        Long memberId = 1L;

        // Then
        assertDoesNotThrow(() -> memberRepository.findByMemberId(memberId));
    }

    @Test
    @DisplayName("존재하지 않는 Member 번호로 Member를 찾는다면 실패.")
    void shouldFindByMemberIdeFailNotExistsMemberId() throws Exception {
        // Given
        Long memberId = 50L;

        // Then
        assertThrows(MemberException.class, () -> memberRepository.findByMemberId(memberId));
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않다면 실패.")
    void shouldVerifyPasswordFailNotMatchesPassword() throws Exception {
        // Given
        String password = memberRepository.findByMemberId(1L).getPassword();
        String inputWrongPassword = "password123";

        // Then
        assertThrows(MemberException.class, () -> memberService.verifyPassword(password, inputWrongPassword));
    }

    @Test
    @DisplayName("Member가 존재하면 토큰을 얻습니다.")
    void shouldGetAccessTokenSuccessfully() throws Exception {
        // Given
        Member member = memberRepository.findByMemberId(1L);

        // When
        String accessToken = memberService.genAccessToken(member);

        // Then
        assertThat(accessToken).isNotNull().isNotEmpty();
    }

    @Test
    @DisplayName("Member가 WhiteList인지 검증합니다.")
    void shouldVerifyWithWhiteListSuccessfully() throws Exception {
        // Given
        Member member = memberRepository.findByMemberId(1L);
        String token = memberService.genAccessToken(member);

        // Then
        assertDoesNotThrow(() -> memberService.verifyWithWhiteList(member, token));
    }

    @Test
    @DisplayName("MemberId가 존재하면 Member의 정보를 얻을 수 있습니다.")
    void shouldGetMemberInfoSuccessfully() throws Exception {
        // Given
        Long memberId = 1L;

        // When
        MyPageResponse myPageResponse = memberService.getMemberInfo(memberId);

        // Then
        assertThat(myPageResponse).isNotNull();
    }

    @Test
    @DisplayName("MemberId가 존재하지 않으면 Member의 정보를 얻을 수 없습니다.")
    void shouldGetMemberInfoFailDueToNotExistsMemberId() throws Exception {
        // Given
        Long memberId = 80L;

        // Then
        assertThrows(MemberException.class, () -> memberService.getMemberInfo(memberId));
    }

    @Test
    @DisplayName("프로필 사진이 존재하지 않으면 프로필 사진을 얻을 수 없습니다.")
    void shouldNotGetMemberProfileImageDueToNotExistsProfileImage() throws Exception {
        // Given
        Long memberId = 1L;

        // When
        ProfileImage profileImage = memberService.getMemberProfileImage(memberId);

        // Then
        assertThat(profileImage).isNull();
    }

}
