package com.example.ot.app.user.controller;

import com.example.ot.app.base.dto.RsData;
import com.example.ot.app.user.dto.UserDTO;
import com.example.ot.app.user.service.UserService;
import com.example.ot.util.Util;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@Tag(name = "normal signup", description = "일반 회원 가입 api")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class SignUpController {

    private final UserService userService;

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

}
