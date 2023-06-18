package com.example.ot.app.host.dto;

import lombok.Getter;

public class HostDTO {

    @Getter
    public static class RegisterHostDTO{

        private String introduction;

        private String hashTag;

        private int city;

    }
}
