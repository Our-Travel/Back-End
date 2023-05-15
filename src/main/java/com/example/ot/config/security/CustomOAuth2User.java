package com.example.ot.config.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private String username;
    private String nickName;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    public CustomOAuth2User(String username, String nickName, Map<String, Object> attributes) {
        this.attributes = attributes;
        this.username = username;
        this.nickName = nickName;
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority("Member"));
    }

    @Override
    public Map<String, Object> getAttributes(){
        return this.attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return this.authorities;
    }

    @Override
    public String getName(){
        return this.username;
    }

}
