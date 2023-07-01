package com.example.ot.app.member.controller;

import com.example.ot.app.base.rsData.RsData;
import com.example.ot.app.member.dto.request.SignInRequest;
import com.example.ot.app.member.dto.request.SignUpRequest;
import com.example.ot.app.member.dto.response.MyPageResponse;
import com.example.ot.app.member.entity.Member;
import com.example.ot.app.member.service.MemberService;
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


@Tag(name = "로그인 및 회원가입")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<RsData> signUp(@Valid @RequestBody SignUpRequest signUpRequest){
        memberService.check(signUpRequest);
        memberService.create(signUpRequest);

        return Util.spring.responseEntityOf(RsData.success("회원가입이 완료되었습니다."));
    }

    @Operation(summary = "아이디 중복체크")
    @GetMapping("/exists/username/{username}")
    public ResponseEntity<RsData> checkUsername(@PathVariable @NotBlank(message = "아이디를 입력해주세요.") String username){
        memberService.checkUsername(username);

        return Util.spring.responseEntityOf(
                RsData.success("S-1", "%s는 사용가능한 이메일입니다.".formatted(username))
        );
    }

    @Operation(summary = "닉네임 중복체크")
    @GetMapping("/exists/nickName/{nickName}")
    public ResponseEntity<RsData> checkNickName(@PathVariable @NotBlank(message = "닉네임을 입력해주세요.") String nickName){
        memberService.checkNickName(nickName);

        return Util.spring.responseEntityOf(
                RsData.success("S-1", "%s는 사용가능한 닉네임입니다.".formatted(nickName))
        );
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<RsData> signIn(@Valid @RequestBody SignInRequest signInRequest){
        Member member = memberService.verifyUsername(signInRequest.getUsername());
        memberService.verifyPassword(member.getPassword(), signInRequest.getPassword());

        String accessToken = memberService.genAccessToken(member);

        return Util.spring.responseEntityOf(
                RsData.success("로그인이 성공적으로 완료되었습니다.",
                        Util.mapOf(
                                "access_token", accessToken
                        )
                ),
                Util.spring.httpHeadersOf("Authentication", accessToken)
        );
    }

    @Operation(summary = "마이페이지", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public ResponseEntity<RsData> showMyPage(@AuthenticationPrincipal MemberContext memberContext) {
        MyPageResponse myPageResponse = memberService.getMemberInfo(memberContext);
        return Util.spring.responseEntityOf(RsData.success("마이페이지입니다.", myPageResponse));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public ResponseEntity<RsData> showProfileEdit(@AuthenticationPrincipal MemberContext memberContext) {
        MyPageResponse myPageResponse = memberService.getMemberInfo(memberContext);
        return Util.spring.responseEntityOf(RsData.success("프로필 편집페이지 입니다.", myPageResponse));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/profile")
    public ResponseEntity<RsData> updateProfile(@RequestParam("images")
                                MultipartFile file, @AuthenticationPrincipal MemberContext memberContext) throws IOException {
        memberService.updateProfile(memberContext.getId(), file);
        return Util.spring.responseEntityOf(RsData.success("프로필 편집완료"));
    }

}
