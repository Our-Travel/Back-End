package com.example.ot.app.host.dto.response;

import com.example.ot.app.host.entity.Host;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class EditHostResponse {
    private String introduction;
    private String hashTag;
    private Integer regionCode;

    public static EditHostResponse fromHost(Host host, String hostHashTag){
        return new EditHostResponse(host.getIntroduction(), hostHashTag, host.getRegionCode());
    }
}
