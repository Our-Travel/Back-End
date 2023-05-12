package com.example.ot.app.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


public class MemberDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SignUpDto {
        @NotBlank(message = "아이디를 입력해주세요.")
        @Email
        private String username;
        @NotBlank(message = "비밀번호를 입력해주세요.")
        private String password;
        @NotBlank(message = "닉네임을 입력해주세요.")
        private String nickName;
        @NotBlank(message = "regionLevel1 을(를) 입력해주세요.")
        private String regionLevel1;
        @NotBlank(message = "regionLevel2 을(를) 입력해주세요.")
        private String regionLevel2;
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
