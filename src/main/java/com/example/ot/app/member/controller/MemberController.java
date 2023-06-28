package com.example.ot.app.member.controller;

import com.example.ot.app.base.rsData.RsData;
import com.example.ot.app.member.dto.request.SignInRequest;
import com.example.ot.app.member.dto.request.SignUpRequest;
import com.example.ot.app.member.service.MemberService;
import com.example.ot.util.Util;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "로그인 및 회원가입")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<RsData> signUp(@Valid @RequestBody SignUpRequest signUpRequest){
        memberService.check(signUpRequest);
        memberService.create(signUpRequest);

        return Util.spring.responseEntityOf(
                RsData.of(
                        "S-1",
                        "회원가입이 완료되었습니다."
                )
        );
    }

    @Operation(summary = "아이디 중복체크")
    @GetMapping("/exists/username/{username}")
    public ResponseEntity<RsData> checkUsername(@PathVariable @NotBlank(message = "아이디를 입력해주세요.") String username){
        memberService.checkUsername(username);

        return Util.spring.responseEntityOf(
                RsData.of(
                        "S-1",
                        "%s는 사용가능한 이메일입니다.".formatted(username)
                )
        );
    }

    @Operation(summary = "닉네임 중복체크")
    @GetMapping("/exists/nickName/{nickName}")
    public ResponseEntity<RsData> checkNickName(@PathVariable @NotBlank(message = "닉네임을 입력해주세요.") String nickName){
        memberService.checkNickName(nickName);

        return Util.spring.responseEntityOf(
                RsData.of(
                        "S-1",
                        "%s는 사용가능한 닉네임입니다.".formatted(nickName)
                )
        );
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<RsData> signIn(@Valid @RequestBody SignInRequest signInRequest){
        memberService.verifyUsername(signInRequest.getUsername());
        memberService.verifyPassword(signInRequest);

        String accessToken = memberService.genAccessToken(signInRequest.getUsername());

        return Util.spring.responseEntityOf(
                RsData.of(
                        "S-1",
                        "로그인이 성공적으로 완료되었습니다.",
                        Util.mapOf(
                                "access_token", accessToken
                        )
                ),
                Util.spring.httpHeadersOf("Authentication", accessToken)
        );
    }

}
