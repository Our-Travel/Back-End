package com.example.ot.config.security;

import com.example.ot.app.member.entity.Member;
import com.example.ot.app.member.repository.MemberRepository;
import com.example.ot.app.member.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class OAuthUserServiceImpl extends DefaultOAuth2UserService {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
        // user-info-uri를 이용해 사용자 정보 가져옴.
        OAuth2User oAuth2User = super.loadUser(userRequest);

        try{
            log.info("OAuth2User attributes {} ", new ObjectMapper().writeValueAsString(oAuth2User.getAttributes()));
        } catch (JsonProcessingException e){
            e.printStackTrace();
        }

        String oauthId = "";
        String providerTypeCode = userRequest.getClientRegistration().getRegistrationId().toUpperCase();

        oauthId = oAuth2User.getName();
        String username = providerTypeCode + "__%s".formatted(oauthId);

        Member member = null;
        // 유저가 존재하지 않으면 새로 생성.
        if(!memberRepository.existsByUsername(username)){
            member = Member.builder()
                    .username(username)
                    .providerTypeCode(providerTypeCode)
                    .nickName(oauthId)
                    .password(passwordEncoder.encode(""))
                    .build();
            member = memberRepository.save(member);
        }
        else{
            member = memberRepository.findByUsername(username).get();
        }
        log.info("Successfully pulled user info username {} authProvider {}",
                username,
                providerTypeCode);
        return new CustomOAuth2User(member.getUsername(), member.getNickName(), oAuth2User.getAttributes());

    }

}
