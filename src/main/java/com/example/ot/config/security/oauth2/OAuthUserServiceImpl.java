package com.example.ot.config.security.oauth2;

import com.example.ot.app.member.dto.request.SignUpRequest;
import com.example.ot.app.member.entity.Member;
import com.example.ot.app.member.exception.MemberException;
import com.example.ot.app.member.repository.MemberRepository;
import com.example.ot.app.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.example.ot.app.member.exception.ErrorCode.USERNAME_NOT_EXISTS;

@RequiredArgsConstructor
@Service
public class OAuthUserServiceImpl extends DefaultOAuth2UserService {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String providerType = userRequest.getClientRegistration().getRegistrationId().toUpperCase();
        String oauthId = "";
        String providerTypeCode = "";
        providerTypeCode = userRequest.getClientRegistration().getRegistrationId().toUpperCase().split("")[0];
        if(providerType.equals("NAVER")){
            Map<String, Object> map = oAuth2User.getAttributes();
            LinkedHashMap<String, String> getNaverId = (LinkedHashMap<String, String>) map.get("response");
            oauthId = getNaverId.get("id");
        }
        else{
            oauthId = oAuth2User.getName().substring(0, 6);
        }
        Member member = saveOrGetMember(oauthId, providerTypeCode, providerType);
        memberService.genAccessToken(member);
        return new CustomOAuth2User(member.getUsername(), member.getNickName(), member.getAuthorities());
    }

    private Member saveOrGetMember(String oauthId, String providerTypeCode, String providerType) {
        String username = oauthId + "@" + providerType.toLowerCase() + ".com";
        if (!memberRepository.existsByUsername(username)) {
            String nickName = providerTypeCode + "__%s".formatted(oauthId);
            SignUpRequest signUp = new SignUpRequest(username, "", nickName);
            return memberService.createMember(providerTypeCode, signUp);
        }
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberException(USERNAME_NOT_EXISTS));
    }

}