package com.example.ot.app.host.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class WriteHostInfoRequest {

    @Size(min = 2, max = 40, message = "자기소개는 2~40자로 입력해주세요.")
    private String introduction;
    private String hashTag;
    private Integer regionCode;
}
