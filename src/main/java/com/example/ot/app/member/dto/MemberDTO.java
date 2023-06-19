package com.example.ot.app.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


public class MemberDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SignUpDto {
        @Schema(description = "아이디")
        @NotBlank(message = "아이디를 입력해주세요.")
        @Email
        private String username;

        @Schema(description = "비밀번호")
        @NotBlank(message = "비밀번호를 입력해주세요.")
        private String password;

        @Schema(description = "닉네임")
        @NotBlank(message = "닉네임을 입력해주세요.")
        @JsonProperty("nick_name")
        private String nickName;

    }

    @Getter
    public static class SignInDto{
        @NotBlank(message = "아이디를 입력해주세요.")
        @Email
        private String username;
        @NotBlank(message = "비밀번호를 입력해주세요.")
        private String password;
    }

}
