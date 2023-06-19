package com.example.ot.app.host.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class HostInfoDTO {
    private String introduction;
    private String hashTag;
    private Integer stateId;
    private Integer cityId;
}
