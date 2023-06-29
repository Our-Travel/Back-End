package com.example.ot.app.host.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class RegisterHostRequest {

    @Size(min = 10, max = 15, message = "자기소개는 10~15자로 입력해주세요.")
    private String introduction;
    private String hashTag;
    private Integer regionCode;
}
