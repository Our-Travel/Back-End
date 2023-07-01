package com.example.ot.app.member.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MyPageResponse {

    private String username;
    @JsonProperty("nick_name")
    private String nickName;
    @JsonProperty("image_path")
    private String imagePath;
}
