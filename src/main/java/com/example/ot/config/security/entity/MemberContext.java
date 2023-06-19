package com.example.ot.config.security.entity;

import com.example.ot.app.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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
@JsonIncludeProperties({"id", "username", "nick_name", "authorities"})
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MemberContext extends User {
    private final long id;
    private final String username;
    private final String nickName;
    private final Set<GrantedAuthority> authorities;

    public MemberContext(Member member) throws IOException {
        super(member.getUsername(), "", member.getAuthorities());

        id = member.getId();
        username = member.getUsername();
        nickName = member.getNickName();
        authorities = member.getAuthorities().stream().collect(Collectors.toSet());
    }
}

