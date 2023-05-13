package com.example.ot.config.security;

import com.example.ot.app.base.dto.RsData;
import com.example.ot.config.security.filter.JwtAuthorizationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.io.IOException;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationConfiguration authenticationConfiguration) throws Exception {
        http
                .authorizeRequests(
                        authorizeRequests -> authorizeRequests
                                .requestMatchers("/api/member/**")
                                .permitAll()
                                .anyRequest()
                                .authenticated() // 최소자격 : 로그인
                )
                .cors().disable() // 타 도메인에서 API 호출 가능
                .csrf().disable() // CSRF 토큰 끄기
                .httpBasic().disable() // httpBaic 로그인 방식 끄기
                .formLogin().disable() // 폼 로그인 방식 끄기
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(STATELESS) // 세션 사용안함
                ).addFilterBefore( // JWT 인증 필터 적용
                        jwtAuthorizationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )
                // 에러 핸들링
                .exceptionHandling()
                // 권한 문제가 발생했을 때 이 부분을 호출한다.
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    response.setCharacterEncoding("utf-8");
                    response.setContentType("application/json; charset=UTF-8");

                    RsData rsData = RsData.of("F-AccessDeniedException", "권한이 없는 사용자입니다.", null);
                    String json = new ObjectMapper().writeValueAsString(rsData);

                    response.getWriter().write(json);
                })
                // 인증문제가 발생했을 때 이 부분을 호출한다.
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.setCharacterEncoding("utf-8");
                    response.setContentType("application/json; charset=UTF-8");

                    RsData rsData = RsData.of("F-UnauthorizedException", "인증되지 않은 사용자입니다.", null);
                    String json = new ObjectMapper().writeValueAsString(rsData);

                    response.getWriter().write(json);
                });

        return http.build();
    }
}
