package com.example.ot.app.host.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class HostInfoListResponse {

    Long memberId;
    String hostProfileImage;
    String nickName;
    String introduction;
    String hashTag;
}
