package com.example.ot.config.security.filter;

import com.example.ot.app.member.entity.Member;
import com.example.ot.app.member.repository.MemberRepository;
import com.example.ot.app.member.service.MemberRedisService;
import com.example.ot.app.member.service.MemberService;
import com.example.ot.config.security.entity.MemberContext;
import com.example.ot.config.security.jwt.JwtUtils;
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
    private final JwtUtils jwtUtils;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final MemberRedisService memberRedisService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null) {
            String token = bearerToken.substring("Bearer ".length());

            if (jwtUtils.verify(token)) {
                Map<String, Object> claims = jwtUtils.getClaims(token);
                long id = (int) claims.get("id");
                Member member = memberRepository.findById(id).orElse(null);
                if(memberService.verifyWithWhiteList(member, token)){
                    forceAuthentication(member);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

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