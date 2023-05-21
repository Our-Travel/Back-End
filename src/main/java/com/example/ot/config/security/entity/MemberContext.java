package com.example.ot.config.security.entity;

import com.example.ot.app.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@JsonIncludeProperties({"id", "username", "nickName", "image", "authorities"})
public class MemberContext extends User {
    private final long id;
    private final String username;
    private final String nickName;
    private final String image;
    private final Set<GrantedAuthority> authorities;

    public MemberContext(Member member) throws IOException {
        super(member.getUsername(), "", member.getAuthorities());

        id = member.getId();
        username = member.getUsername();
        nickName = member.getNickName();
        image = getImageBase64Encode(member);
        authorities = member.getAuthorities().stream().collect(Collectors.toSet());
    }

    // 이미지를 base64로 인코딩
    private String getImageBase64Encode(Member member) throws IOException {
        String filePath = member.getProfileImage().getStoredFilePath();
        String extension = member.getProfileImage().getExtension().substring(1);
        if(extension.equals("jpg")){
            extension = "jpeg";
        }
        File file = new File(filePath);
        byte[] fileBytes = Files.readAllBytes(file.toPath());
        String base64File = Base64.getEncoder().encodeToString(fileBytes);
        String base64ImageString = "data:image/"+extension +";base64," + base64File;
        return base64ImageString;
    }
}

