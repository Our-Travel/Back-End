package com.example.ot.app.member.controller;

import com.example.ot.app.member.dto.request.InputPasswordRequest;
import com.example.ot.app.member.dto.request.SignInRequest;
import com.example.ot.app.member.dto.request.SignUpRequest;
import com.example.ot.app.member.dto.request.UpdateMemberRequest;
import com.example.ot.app.member.dto.response.MyPageResponse;
import com.example.ot.app.member.entity.Member;
import com.example.ot.app.member.service.MemberService;
import com.example.ot.base.rsData.RsData;
import com.example.ot.config.security.entity.MemberContext;
import com.example.ot.util.Util;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.example.ot.app.member.code.MemberSuccessCode.*;


@Tag(name = "로그인 및 회원가입")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<RsData> signUp(@Valid @RequestBody SignUpRequest signUpRequest){
        memberService.createMember(signUpRequest);
        return Util.spring.responseEntityOf(RsData.success(SIGNUP_CREATED));
    }

    @Operation(summary = "아이디 중복체크")
    @GetMapping("/exists/username/{username}")
    public ResponseEntity<RsData> checkUsername(@PathVariable @NotBlank(message = "아이디를 입력해주세요.") String username){
        memberService.checkUsername(username);
        return Util.spring.responseEntityOf(RsData.success(EMAIL_AVAILABLE.formatted(username)));
    }

    @Operation(summary = "닉네임 중복체크")
    @GetMapping("/exists/nickName/{nickName}")
    public ResponseEntity<RsData> checkNickName(@PathVariable @NotBlank(message = "닉네임을 입력해주세요.") String nickName){
        memberService.checkNickName(nickName);
        return Util.spring.responseEntityOf(RsData.success(NICKNAME_AVAILABLE.formatted(nickName)));
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<RsData> signIn(@Valid @RequestBody SignInRequest signInRequest){
        String accessToken = memberService.validSignInAndGetAccessToken(signInRequest);
        return Util.spring.responseEntityOf(RsData.success(LOGIN_COMPLETED),
                Util.spring.httpHeadersOf("Authentication", accessToken)
        );
    }

    @Operation(summary = "마이페이지", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<RsData> showMyPage(@AuthenticationPrincipal MemberContext memberContext) {
        MyPageResponse myPageResponse = memberService.getMemberInfo(memberContext.getId());
        return Util.spring.responseEntityOf(RsData.success(MY_PAGE, myPageResponse));
    }

    @Operation(summary = "비밀번호가 올바른지 검증", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/validate-password")
    public ResponseEntity<RsData> verifyPassword(@RequestBody InputPasswordRequest inputPasswordRequest,
                                                 @AuthenticationPrincipal MemberContext memberContext) {
        memberService.verifyPassword(memberContext.getId(), inputPasswordRequest.getPassword());
        return Util.spring.responseEntityOf(RsData.success(PASSWORD_CORRECTED));
    }

    @Operation(summary = "프로필 편집 페이지", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public ResponseEntity<RsData> showProfileEdit(@AuthenticationPrincipal MemberContext memberContext) {
        MyPageResponse myPageResponse = memberService.getMemberInfo(memberContext.getId());
        return Util.spring.responseEntityOf(RsData.success(PROFILE_EDIT_PAGE, myPageResponse));
    }

    @Operation(summary = "프로필 사진 변경", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/profile-image")
    public ResponseEntity<RsData> updateProfileImage(@RequestParam("images")
                                MultipartFile file, @AuthenticationPrincipal MemberContext memberContext) throws IOException {
        memberService.updateProfileImage(memberContext.getId(), file);
        return Util.spring.responseEntityOf(RsData.success(PROFILE_IMAGE_UPDATED));
    }

    @Operation(summary = "기본 프로필 변경", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/profile-image")
    public ResponseEntity<RsData> deleteProfileImage(@AuthenticationPrincipal MemberContext memberContext) {
        memberService.deleteProfileImage(memberContext.getId());
        return Util.spring.responseEntityOf(RsData.success(PROFILE_IMAGE_DELETED));
    }

    @Operation(summary = "프로필 정보 변경", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/profile")
    public ResponseEntity<RsData> updateProfileInfo(@Valid @RequestBody UpdateMemberRequest updateMemberRequest,
                                                    @AuthenticationPrincipal MemberContext memberContext) {
        memberService.updateMemberInfo(updateMemberRequest, memberContext.getId());
        return Util.spring.responseEntityOf(RsData.success(PROFILE_UPDATED));
    }

}
