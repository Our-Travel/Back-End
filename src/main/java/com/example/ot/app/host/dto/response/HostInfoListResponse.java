package com.example.ot.app.host.dto.response;

import com.example.ot.app.host.entity.Host;
import com.example.ot.app.member.entity.ProfileImage;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class HostInfoListResponse {

    private Long memberId;
    private Long hostId;
    private String hostProfileImage;
    private String nickName;
    private String introduction;
    private String hashTag;
    private boolean chatRoomExist;
    private boolean hostMember;

    public static HostInfoListResponse fromHost(Host host, String hashTag, ProfileImage hostProfileImage){
        String hostProfileImageUrl = (hostProfileImage != null) ? hostProfileImage.getFullPath() : null;

        return HostInfoListResponse.builder()
                .memberId(host.getMember().getId())
                .hostId(host.getId())
                .hashTag(hashTag)
                .nickName(host.getMember().getNickName())
                .introduction(host.getIntroduction())
                .hostProfileImage(hostProfileImageUrl)
                .build();
    }

    public void updateChatRoomExist(){
        this.chatRoomExist = true;
    }

    public void updateHostMember(){
        this.hostMember = true;
    }
}
