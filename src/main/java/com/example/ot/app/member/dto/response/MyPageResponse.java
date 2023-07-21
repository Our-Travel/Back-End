package com.example.ot.app.member.dto.response;

import com.example.ot.app.member.entity.Member;
import com.example.ot.app.member.entity.ProfileImage;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MyPageResponse {

    private String username;
    @JsonProperty("nick_name")
    private String nickName;
    @JsonProperty("image_path")
    private String imagePath;
    private boolean hostAuthority;

    public static MyPageResponse fromMember(Member member, ProfileImage profileImage){
        if(ObjectUtils.isEmpty(profileImage)){
            return new MyPageResponse(member.getUsername(), member.getNickName(), null, member.isHostAuthority());
        }
        return new MyPageResponse(member.getUsername(), member.getNickName(), profileImage.getFullPath(), member.isHostAuthority());
    }
}
