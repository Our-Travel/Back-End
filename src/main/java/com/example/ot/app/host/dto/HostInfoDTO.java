package com.example.ot.app.host.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HostInfoDTO {
    private String introduction;
    private String hashTag;
    private Integer stateId;
    private Integer cityId;
}
