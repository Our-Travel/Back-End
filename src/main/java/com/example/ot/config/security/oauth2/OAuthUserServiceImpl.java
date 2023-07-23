package com.example.ot.config.security.oauth2;

import com.example.ot.app.member.dto.request.SignUpRequest;
import com.example.ot.app.member.entity.Member;
import com.example.ot.app.member.repository.MemberRepository;
import com.example.ot.app.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OAuthUserServiceImpl extends DefaultOAuth2UserService {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String providerTypeCode = userRequest.getClientRegistration().getRegistrationId().toUpperCase();
        String oauthId = oAuth2User.getName();
        String nickName = providerTypeCode + "__%s".formatted(oauthId);

        Member member = saveOrGetMember(nickName, providerTypeCode);
        return new CustomOAuth2User(member.getUsername(), member.getNickName(), member.getAuthorities());
    }

    private Member saveOrGetMember(String nickName, String providerTypeCode) {
        String username = nickName + "@example.com";
        if (!memberRepository.existsByUsername(username)) {
            SignUpRequest signUp = new SignUpRequest(username, "", nickName);
            return memberService.create(providerTypeCode, signUp);
        }
        return memberService.findByUsername(username);
    }

}