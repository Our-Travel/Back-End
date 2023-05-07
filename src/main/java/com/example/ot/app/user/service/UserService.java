package com.example.ot.app.user.service;

import com.example.ot.app.user.dto.UserDTO;
import com.example.ot.app.user.entity.User;
import com.example.ot.app.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void create(UserDTO userDTO){
        log.info("회원가입 완료");
        User user = User.builder()
                .email(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .nickName(userDTO.getNickName())
                .regionLevel1(userDTO.getRegionLevel1())
                .regionLevel2(userDTO.getRegionLevel2())
                .build();

        log.info("회원가입 완료");
        userRepository.save(user);
    }
}