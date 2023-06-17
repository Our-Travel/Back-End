package com.example.ot.app.host.dto;

import lombok.Builder;
import lombok.Getter;

public class HostDTO {

    @Getter
    @Builder
    public static class registerHostDTO{

        private String introduction;

        private String hashTag;

        private int city;

    }
}
