package com.example.ot.app.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignInRequest {
    @NotBlank(message = "아이디를 입력해주세요.")
    @Email
    private String username;
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
}
