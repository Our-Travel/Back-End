package com.example.ot.app.host.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class RegisterHostDTO {

    private String introduction;
    private String hashTag;
    private String region;

}
