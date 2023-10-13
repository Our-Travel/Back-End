package com.example.ot.config.security;

import com.example.ot.config.security.entryPoint.ApiAuthenticationEntryPoint;
import com.example.ot.config.security.filter.JwtAuthorizationFilter;
import com.example.ot.config.security.oauth2.OAuth2MemberSuccessHandler;
import com.example.ot.config.security.oauth2.OAuthUserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
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
    private final OAuthUserServiceImpl oAuthUserService;
    private final OAuth2MemberSuccessHandler oAuth2MemberSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationConfiguration authenticationConfiguration) throws Exception {
        http
                .securityMatcher("/**")
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(authenticationEntryPoint)
                )
                .authorizeHttpRequests(
                        authorizeRequests -> authorizeRequests
                                .requestMatchers("/members/**", "/oauth2/**", "/boards/list/**","/ws/chat/**","/**",
                                        "/swagger-ui/**", "/swagger-resources/**",
                                        "/v2/api-docs/**", "/v3/api-docs/**", "/webjars/**")
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .cors().and()
                .csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(STATELESS)
                )
                .logout(
                        AbstractHttpConfigurer::disable
                )
                .oauth2Login(oauth2 -> oauth2
                        .redirectionEndpoint()
                        .baseUri("/oauth2/callback/*")
                        .and()
                        .successHandler(oAuth2MemberSuccessHandler)
                        .userInfoEndpoint()
                        .userService(oAuthUserService))
                .addFilterBefore(
                        jwtAuthorizationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://www.ourtravel.site")
                .allowedMethods("*")
                .allowedHeaders("*")
                .exposedHeaders("Authentication");
    }
}
