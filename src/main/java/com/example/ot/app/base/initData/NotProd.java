package com.example.ot.app.base.initData;

import com.example.ot.app.member.dto.MemberDTO;
import com.example.ot.app.member.service.MemberService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@Profile({"dev", "test"})
public class NotProd {
    @Bean
    CommandLineRunner initData(
            MemberService memberService
    ) {
        return new CommandLineRunner() {
            @Override
            @Transactional
            public void run(String... args) throws Exception {
                MemberDTO.SignUpDto user1 = new MemberDTO.SignUpDto("user1@example.com", "1234", "user1");
                MemberDTO.SignUpDto user2 = new MemberDTO.SignUpDto("user2@example.com", "1234", "user2");
                memberService.create(user1);
                memberService.create(user2);
            }
        };
    }
}
