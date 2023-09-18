package com.example.ot.base.init;

import com.example.ot.app.member.dto.request.SignUpRequest;
import com.example.ot.app.member.service.MemberService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"dev", "local", "test"})
public class initData {
    @Bean
    CommandLineRunner init(
            MemberService memberService
    ){
        return args -> {
            SignUpRequest memeber1 = new SignUpRequest("test1@test.com", "qwe123!!", "test1");
            SignUpRequest memeber2 = new SignUpRequest("test2@test.com", "qwe123!!", "test2");
            SignUpRequest memeber3 = new SignUpRequest("test3@test.com", "qwe123!!", "test3");
            SignUpRequest memeber4 = new SignUpRequest("test4@test.com", "qwe123!!", "test4");
            memberService.createMember(memeber1);
            memberService.createMember(memeber2);
            memberService.createMember(memeber3);
            memberService.createMember(memeber4);
        };
    }
}
