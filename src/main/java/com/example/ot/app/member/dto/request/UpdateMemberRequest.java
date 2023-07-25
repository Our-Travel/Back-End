package com.example.ot.app.member.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UpdateMemberRequest {
    private String password;
    @JsonProperty("verify_password")
    private String verifyPassword;
    @JsonProperty("nick_name")
    private String nickName;
}
