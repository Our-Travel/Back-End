package com.example.ot.app.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.SuperBuilder;


public class UserDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SignUpDto {
        @NotBlank(message = "email 을(를) 입력해주세요.")
        @Email
        private String email;
        @NotBlank(message = "password 을(를) 입력해주세요.")
        private String password;
        @NotBlank(message = "nickName 을(를) 입력해주세요.")
        private String nickName;
        @NotBlank(message = "regionLevel1 을(를) 입력해주세요.")
        private String regionLevel1;
        @NotBlank(message = "regionLevel2 을(를) 입력해주세요.")
        private String regionLevel2;
    }

}
