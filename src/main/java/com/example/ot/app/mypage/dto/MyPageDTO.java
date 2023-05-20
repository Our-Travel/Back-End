package com.example.ot.app.mypage.dto;

import com.example.ot.config.security.entity.MemberContext;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;

public class MyPageDTO {

    @Setter
    public static class ProfileImageWithUserDto{

        private MemberContext memberContext;
        @JsonProperty("profilePicture")
        private String profilePicture;
    }

}
