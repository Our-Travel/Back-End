package com.example.ot.app.member.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateMemberRequest {

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, max = 16, message = "비밀번호는 8~16자 사이어야 합니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*()]).{0,}", message = "비밀번호는 숫자, 영문, 특수문자를 모두 포함해야 합니다.")
    private String password;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, max = 16, message = "비밀번호는 8~16자 사이어야 합니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*()]).{0,}", message = "비밀번호는 숫자, 영문, 특수문자를 모두 포함해야 합니다.")
    @JsonProperty("verify_password")
    private String verifyPassword;

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(min = 3, max = 15, message = "닉네임은 3~8자 사이어야 합니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]*$", message = "닉네임은 한글, 영문, 숫자만 가능합니다.")
    @JsonProperty("nick_name")
    private String nickName;
}
