package com.example.ot.app.member.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateMemberRequest {

    private String password;

    @JsonProperty("verify_password")
    private String verifyPassword;

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(min = 3, max = 15, message = "닉네임은 3~8자 사이어야 합니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]*$", message = "닉네임은 한글, 영문, 숫자만 가능합니다.")
    @JsonProperty("nick_name")
    private String nickName;
}
