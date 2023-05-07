package com.example.ot.app.user.controller;

import com.example.ot.app.base.dto.RsData;
import com.example.ot.app.user.dto.UserDTO;
import com.example.ot.app.user.service.UserService;
import com.example.ot.util.Util;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Tag(name = "normal signup", description = "일반 회원 가입 api")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class SignUpController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<RsData> signUp(@Valid @RequestBody UserDTO.SignUpUserDto signUpUserDto){
        log.info("email : {} " , signUpUserDto.getEmail());
        log.info("password : {} " , signUpUserDto.getPassword());
        log.info("nickName : {} " , signUpUserDto.getNickName());
        log.info("regionLevel1 : {} " , signUpUserDto.getRegionLevel1());
        log.info("regionLevel2 : {} " , signUpUserDto.getRegionLevel2());

        userService.create(signUpUserDto);
        return Util.spring.responseEntityOf(
                RsData.of(
                        "S-1",
                        "회원가입 완료"
                )
        );
    }

    // 이메일 중복체크
    @GetMapping("/check-email/{email}")
    public ResponseEntity<RsData> checkEmail(@PathVariable String email){
        if(userService.checkEmail(email)){
            return Util.spring.responseEntityOf(
                    RsData.of(
                            "F-1",
                            "중복된 이메일입니다."
                    )
            );
        }
        return Util.spring.responseEntityOf(
                RsData.of(
                        "S-1",
                        "%s는 사용가능한 이메일입니다.".formatted(email)
                )
        );
    }

    // 닉네임 중복체크
    @GetMapping("/check-nickName/{nickName}")
    public ResponseEntity<RsData> checkNickName(@PathVariable String nickName){
        if(userService.checkNickName(nickName)){
            return Util.spring.responseEntityOf(
                    RsData.of(
                            "F-1",
                            "중복된 닉네임입니다."
                    )
            );
        }
        return Util.spring.responseEntityOf(
                RsData.of(
                        "S-1",
                        "%s는 사용가능한 닉네임입니다.".formatted(nickName)
                )
        );
    }

}
