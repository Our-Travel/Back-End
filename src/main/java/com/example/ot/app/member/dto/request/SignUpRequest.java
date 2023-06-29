package com.example.ot.app.member.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {
    @Schema(description = "아이디")
    @NotBlank(message = "아이디를 입력해주세요.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String username;

    @Schema(description = "비밀번호")
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, max = 16, message = "비밀번호는 8~16자 사이어야 합니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*()]).{0,}", message = "비밀번호는 숫자, 영문, 특수문자를 모두 포함해야 합니다.")
    private String password;

    @Schema(description = "닉네임")
    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(min = 3, max = 8, message = "닉네임은 3~8자 사이어야 합니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]*$", message = "닉네임은 한글, 영문, 숫자만 가능합니다.")
    @JsonProperty("nick_name")
    private String nickName;
}
