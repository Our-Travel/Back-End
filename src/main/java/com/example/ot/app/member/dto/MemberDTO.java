package com.example.ot.app.member.dto;

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
        private String nickName;

        @Schema(description = "ex) 서울특별시, 인천광역시")
        @NotBlank(message = "regionLevel1 을(를) 입력해주세요.")
        private String regionLevel1;

        @Schema(description = "ex) 관악구, 강릉시, 화성시")
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
