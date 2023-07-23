package com.example.ot.app.host.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class WriteHostInfoRequest {

    @Size(min = 2, max = 40, message = "자기소개는 2 ~ 40자로 입력해주세요.")
    @NotBlank(message = "자기소개를 입력해주세요.")
    private String introduction;
    @NotBlank(message = "해시태그를 입력해주세요.")
    private String hashTag;
    @NotNull(message = "지역코드를 입력해주세요.")
    private Integer regionCode;
}
