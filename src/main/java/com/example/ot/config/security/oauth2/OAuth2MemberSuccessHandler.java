package com.example.ot.config.security.oauth2;

import com.example.ot.app.member.entity.Member;
import com.example.ot.app.member.exception.MemberException;
import com.example.ot.app.member.repository.MemberRepository;
import com.example.ot.app.member.service.MemberService;
import com.example.ot.base.rsData.RsData;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

import static com.example.ot.app.member.code.MemberSuccessCode.LOGIN_COMPLETED;
import static com.example.ot.app.member.exception.ErrorCode.USERNAME_NOT_EXISTS;

@RequiredArgsConstructor
@Slf4j
@Component
public class OAuth2MemberSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String username = oAuth2User.getUsername();
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberException(USERNAME_NOT_EXISTS));;
        String accessToken = memberService.genAccessToken(member);
        String requestURI = request.getRequestURI();
        String url = makeRedirectUrl(requestURI, accessToken);
        getRedirectStrategy().sendRedirect(request, response, url);

//        response.addHeader("Authentication", accessToken);
//        RsData<Object> rsData = RsData.success(LOGIN_COMPLETED);
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        response.setStatus(HttpServletResponse.SC_OK);
//        String jsonResponse = objectMapper.writeValueAsString(rsData);
//        response.getWriter().write(jsonResponse);
    }

    private String makeRedirectUrl(String requestURI, String token) {
//        int index = requestURI.indexOf("/api/prod");
//        String firstPart = requestURI.substring(0, index);
//        String secondPart = requestURI.substring(index + "/api/prod".length());
//        String url = firstPart + secondPart;
        return UriComponentsBuilder.fromUriString(requestURI + "/oauth2/redirect/" + token)
                .build().toUriString();
    }
}
