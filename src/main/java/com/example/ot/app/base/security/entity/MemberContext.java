package com.example.ot.app.base.security.entity;

import com.example.ot.app.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@JsonIncludeProperties({"id", "createdDate", "modifiedDate", "username", "nickName", "authorities"})
public class MemberContext extends User {
    private final long id;
    private final LocalDateTime createdDate;
    private final LocalDateTime modifiedDate;
    private final String username;
    private final String nickName;
    private final Set<GrantedAuthority> authorities;

    public MemberContext(Member member) {
        super(member.getUsername(), "", member.getAuthorities());

        id = member.getId();
        createdDate = member.getCreatedDate();
        modifiedDate = member.getModifiedDate();
        username = member.getUsername();
        nickName = member.getNickName();
        authorities = member.getAuthorities().stream().collect(Collectors.toSet());
    }
}
