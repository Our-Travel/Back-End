package com.example.ot.config.security.filter;

import com.example.ot.app.member.entity.Member;
import com.example.ot.app.member.service.MemberService;
import com.example.ot.config.security.entity.MemberContext;
import com.example.ot.config.security.jwt.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final MemberService memberService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String bearerToken = request.getHeader("Authorization"); // 헤더에서 Authorization를 가져옴.

        if (bearerToken != null) {
            String token = bearerToken.substring("Bearer ".length()); // 토큰을 받아온다.

            // 토큰이 유효한지 체크
            if (jwtProvider.verify(token)) {
                Map<String, Object> claims = jwtProvider.getClaims(token);
                long id = (int) claims.get("id");
                // 캐시(레디스) 사용
//                Member member = memberService.getByMemberId__cached(id);
                Member member = memberService.findById(id);
                if(memberService.verifyWithWhiteList(member, token)){
                    forceAuthentication(member); // member 찾았으면 인증.
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    // 인가
    private void forceAuthentication(Member member) throws IOException {
        MemberContext memberContext = new MemberContext(member);

        UsernamePasswordAuthenticationToken authentication =
                UsernamePasswordAuthenticationToken.authenticated(
                        memberContext,
                        null,
                        member.getAuthorities()
                );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }
}