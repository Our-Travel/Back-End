package com.example.ot.app.host.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HostInfoResponse {
    private HostInfoDTO hostInfoDTO;
    private RegionDTO regionDTO;
}
