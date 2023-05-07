package com.example.ot.app.base.initData;

import com.example.ot.app.user.dto.UserDTO;
import com.example.ot.app.user.entity.User;
import com.example.ot.app.user.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@Profile({"dev", "test"})
public class NotProd {
    @Bean
    CommandLineRunner initData(
            UserService userService
    ) {
        return new CommandLineRunner() {
            @Override
            @Transactional
            public void run(String... args) throws Exception {
                UserDTO.SignUpDto user1 = new UserDTO.SignUpDto("user1@example.com", "1234", "user1", "서울특별시", "관악구");
                UserDTO.SignUpDto user2 = new UserDTO.SignUpDto("user2@example.com", "1234", "user2", "서울특별시", "관악구");
                userService.create(user1);
                userService.create(user2);
            }
        };
    }
}
