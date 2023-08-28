package com.example.ot.app.host.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class HostCountResponse {

    private int regionCode;
    private long count;

    public HostCountResponse(int regionCode, Long count) {
        this.regionCode = regionCode;
        this.count = count != null ? count.intValue() : 0;
    }
}
