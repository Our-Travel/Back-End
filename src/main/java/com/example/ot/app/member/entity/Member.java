package com.example.ot.app.member.entity;

import com.example.ot.app.base.entity.BaseTimeEntity;
import com.example.ot.app.mypage.entity.ProfileImage;
import com.example.ot.util.Util;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseTimeEntity {

    @Column(unique = true)
    private String username;

    @JsonIgnore
    private String password;

    @Column(unique = true)
    private String nickName;

    @Column(columnDefinition = "TEXT")
    @Setter
    private String accessToken;

    private String providerTypeCode;

    @OneToOne(cascade = CascadeType.ALL)
    @Setter
    private ProfileImage profileImage;

    // 현재 회원이 가지고 있는 권한들을 List<GrantedAuthority> 형태로 리턴
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("MEMBER"));

        // 관리자 계정
        if (username.equals("admin@example.com")) {
            authorities.add(new SimpleGrantedAuthority("admin"));
        }

        return authorities;
    }

    public Map<String, Object> getAccessTokenClaims() {
        return Util.mapOf(
                "id", getId(),
                "username", getUsername(),
                "nickName", getNickName(),
                "authorities", getAuthorities()
        );
    }
}
