package com.example.ot.app.user.controller;

import com.example.ot.app.base.dto.RsData;
import com.example.ot.app.user.dto.UserDTO;
import com.example.ot.app.user.entity.User;
import com.example.ot.app.user.service.UserService;
import com.example.ot.util.Util;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@Tag(name = "normal signup", description = "일반 회원 가입 api")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<RsData> signUp(@Valid @RequestBody UserDTO.SignUpDto signUpDto){
        log.info("email : {} " , signUpDto.getEmail());
        log.info("password : {} " , signUpDto.getPassword());
        log.info("nickName : {} " , signUpDto.getNickName());
        log.info("regionLevel1 : {} " , signUpDto.getRegionLevel1());
        log.info("regionLevel2 : {} " , signUpDto.getRegionLevel2());

        RsData<User> check = userService.check(signUpDto);

        // 중복된 것이 있을 경우.
        if(check.isFail()){
            return Util.spring.responseEntityOf(check);
        }
        userService.create(signUpDto);
        return Util.spring.responseEntityOf(
                RsData.of(
                        "S-1",
                        "회원가입 완료"
                )
        );
    }

    // 이메일 중복체크
    @GetMapping("/check-email/{email}")
    public ResponseEntity<RsData> checkEmail(@PathVariable @NotBlank(message = "이메일을 입력해주세요.") String email){
        RsData<User> checkEmail = userService.checkEmail(email);
        if(checkEmail.isFail()){
            return Util.spring.responseEntityOf(checkEmail);
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
    public ResponseEntity<RsData> checkNickName(@PathVariable @NotBlank(message = "닉네임을 입력해주세요.") String nickName){
        RsData<User> checkNickName = userService.checkNickName(nickName);
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

}
