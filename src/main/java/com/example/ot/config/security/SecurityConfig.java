package com.example.ot.config.security;

import com.example.ot.app.base.rsData.RsData;
import com.example.ot.config.security.entryPoint.ApiAuthenticationEntryPoint;
import com.example.ot.config.security.filter.JwtAuthorizationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig implements WebMvcConfigurer {

    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    private final ApiAuthenticationEntryPoint authenticationEntryPoint;
    private final OAuthSuccessHandler oAuthSuccessHandler;
    private final OAuthUserServiceImpl oAuthUserService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**")
                .exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint(authenticationEntryPoint)
        )
                .authorizeHttpRequests(
                        authorizeRequests -> authorizeRequests
                                .requestMatchers("/api/member/**", "/auth/**", "/oauth2/**")
                                .permitAll()
                                .anyRequest()
                                .authenticated() // 최소자격 : 로그인
                )
                .cors().and() // cors 활성화
                .csrf().disable() // CSRF 토큰 끄기
                .httpBasic().disable() // httpBaic 로그인 방식 끄기
                .formLogin().disable() // 폼 로그인 방식 끄기
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(STATELESS) // 세션 사용안함
                );
//                .oauth2Login()
//                        .redirectionEndpoint().baseUri("/oauth2/callback/*")
//                        .and().authorizationEndpoint().baseUri("/auth/zuthorize").and()
//                                .userInfoEndpoint()
//                                        .userService(oAuthUserService).and().successHandler(oAuthSuccessHandler)
//                        .and().exceptionHandling().authenticationEntryPoint(new Http403ForbiddenEntryPoint());
//                .loginPage("/oauth2Login")
//                .redirectionEndpoint()
//                .baseUri("/oauth2/callback/*") // 디폴트는 login/oauth2/code/*
//                .and()
//                .userInfoEndpoint().userService(oAuthUserService)
//                .and()
//                .successHandler(oAuthSuccessHandler);
                http.addFilterBefore( // JWT 인증 필터 적용
                        jwtAuthorizationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );
                //Oauth2 설정
//                .oauth2Login().userInfoEndpoint().userService(oAuthUserService).and().successHandler(oAuthSuccessHandler);

        return http.build();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(true);
    }
}
