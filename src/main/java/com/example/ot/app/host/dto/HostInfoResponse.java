package com.example.ot.app.host.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HostInfoResponse {
    @JsonProperty("host_info")
    private HostInfoDTO hostInfoDTO;
    @JsonProperty("region")
    private RegionDTO regionDTO;
}
