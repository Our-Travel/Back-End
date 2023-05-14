package com.example.ot.app.member.controller;

import com.example.ot.app.base.dto.RsData;
import com.example.ot.app.member.dto.MemberDTO;
import com.example.ot.app.member.entity.Member;
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
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<RsData> signUp(@Valid @RequestBody MemberDTO.SignUpDto signUpDto){
        log.info("username : {} " , signUpDto.getUsername());
        log.info("password : {} " , signUpDto.getPassword());
        log.info("nickName : {} " , signUpDto.getNickName());
        log.info("regionLevel1 : {} " , signUpDto.getRegionLevel1());
        log.info("regionLevel2 : {} " , signUpDto.getRegionLevel2());

        RsData<Member> check = memberService.check(signUpDto);

        // 중복된 것이 있을 경우.
        if(check.isFail()){
            return Util.spring.responseEntityOf(check);
        }
        memberService.create(signUpDto);
        return Util.spring.responseEntityOf(
                RsData.of(
                        "S-1",
                        "회원가입 완료"
                )
        );
    }

    @Operation(summary = "아이디 중복체크")
    @GetMapping("/check-username/{username}")
    public ResponseEntity<RsData> checkUsername(@PathVariable @NotBlank(message = "아이디를 입력해주세요.") String username){
        RsData<Member> checkUsername = memberService.checkUsername(username);
        if(checkUsername.isFail()){
            return Util.spring.responseEntityOf(checkUsername);
        }
        return Util.spring.responseEntityOf(
                RsData.of(
                        "S-1",
                        "%s는 사용가능한 이메일입니다.".formatted(username)
                )
        );
    }

    @Operation(summary = "닉네임 중복체크")
    @GetMapping("/check-nickName/{nickName}")
    public ResponseEntity<RsData> checkNickName(@PathVariable @NotBlank(message = "닉네임을 입력해주세요.") String nickName){
        RsData<Member> checkNickName = memberService.checkNickName(nickName);
        if(checkNickName.isFail()){
            return Util.spring.responseEntityOf(checkNickName);
        }
        return Util.spring.responseEntityOf(
                RsData.of(
                        "S-1",
                        "%s는 사용가능한 닉네임입니다.".formatted(nickName)
                )
        );
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<RsData> signIn(@Valid @RequestBody MemberDTO.SignInDto signInDto){
        Member member = memberService.findByUsername(signInDto.getUsername()).orElse(null);

        if(member == null){
            return Util.spring.responseEntityOf(RsData.of("F-1", "일치하는 회원이 존재하지 않습니다."));
        }

        if (passwordEncoder.matches(signInDto.getPassword(), member.getPassword()) == false) {
            return Util.spring.responseEntityOf(RsData.of("F-1", "비밀번호가 일치하지 않습니다."));
        }

        log.debug("Util.json.toStr(user.getAccessTokenClaims()) : " + Util.json.toStr(member.getAccessTokenClaims()));

        String accessToken = memberService.genAccessToken(member);

        return Util.spring.responseEntityOf(
                RsData.of(
                        "S-1",
                        "로그인 성공, Access Token을 발급합니다."
                ),
                Util.spring.httpHeadersOf("Authentication", accessToken)
        );
    }

}
