package com.example.ot.app.member.entity;

import com.example.ot.app.member.dto.request.SignUpRequest;
import com.example.ot.base.entity.BaseTimeEntity;
import com.example.ot.util.Util;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE member SET deleted_date = NOW() where id = ?")
@Where(clause = "deleted_date is NULL")
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

    @Setter
    private boolean hostAuthority = false;

    // 현재 회원이 가지고 있는 권한들을 List<GrantedAuthority> 형태로 리턴
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("MEMBER"));

        // 관리자 계정
        if (username.equals("admin@example.com")) {
            authorities.add(new SimpleGrantedAuthority("admin"));
        }
        if(hostAuthority){
            authorities.add(new SimpleGrantedAuthority("HOST"));
        }
        return authorities;
    }

    public static Member of(String providerTypeCode, SignUpRequest signUpRequest, String password){
        return Member.builder()
                .username(signUpRequest.getUsername())
                .password(password)
                .nickName(signUpRequest.getNickName())
                .providerTypeCode(providerTypeCode)
                .build();
    }

    public Map<String, Object> getAccessTokenClaims() {
        return Util.mapOf(
                "id", getId(),
                "username", getUsername(),
                "nickName", getNickName(),
                "authorities", getAuthorities()
        );
    }

    public Map<String, Object> toMap() {
        return Util.mapOf(
                "id", getId(),
                "createdDate", getCreatedDate(),
                "modifiedDate", getModifiedDate(),
                "username", getUsername(),
                "nickName", getNickName(),
                "accessToken", getAccessToken(),
                "authorities", getAuthorities(),
                "providerTypeCode", getProviderTypeCode(),
                "hostAuthority", isHostAuthority()
        );
    }

    public static Member fromMap(Map<String, Object> map) {
        return fromJwtClaims(map);
    }

    public static Member fromJwtClaims(Map<String, Object> jwtClaims) {
        long id = 0;

        if (jwtClaims.get("id") instanceof Long) {
            id = (long) jwtClaims.get("id");
        } else if (jwtClaims.get("id") instanceof Integer) {
            id = (long) (int) jwtClaims.get("id");
        }

        LocalDateTime createdDate = null;
        LocalDateTime modifiedDate = null;

        if (jwtClaims.get("createDate") instanceof List) {
            createdDate = Util.date.bitsToLocalDateTime((List<Integer>) jwtClaims.get("createdDate"));
        }

        if (jwtClaims.get("modifyDate") instanceof List) {
            modifiedDate = Util.date.bitsToLocalDateTime((List<Integer>) jwtClaims.get("modifiedDate"));
        }

        String username = (String) jwtClaims.get("username");
        String accessToken = (String) jwtClaims.get("accessToken");
        String nickName = (String) jwtClaims.get("nickName");
        String providerTypeCode = (String) jwtClaims.get("providerTypeCode");
        boolean hostAuthority = (boolean) jwtClaims.get("hostAuthority");

        return Member
                .builder()
                .id(id)
                .createdDate(createdDate)
                .modifiedDate(modifiedDate)
                .username(username)
                .accessToken(accessToken)
                .nickName(nickName)
                .providerTypeCode(providerTypeCode)
                .hostAuthority(hostAuthority)
                .build();
    }

    public void updateNickName(String nickName) {
        this.nickName = nickName;
    }

    public void updatePassword(String password) {
        this.password = password;
    }
}
