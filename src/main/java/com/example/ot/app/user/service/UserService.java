package com.example.ot.app.user.service;

import com.example.ot.app.user.dto.UserDTO;
import com.example.ot.app.user.entity.User;
import com.example.ot.app.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입 생성.
    @Transactional
    public void create(UserDTO.SignUpUserDto signUpUserDto){
        User user = User.builder()
                .email(signUpUserDto.getEmail())
                .password(passwordEncoder.encode(signUpUserDto.getPassword()))
                .nickName(signUpUserDto.getNickName())
                .regionLevel1(signUpUserDto.getRegionLevel1())
                .regionLevel2(signUpUserDto.getRegionLevel2())
                .build();
        
        userRepository.save(user);
        log.info("회원가입 완료");
    }

    // 이메일 중복체크.
    public boolean checkEmail(String email){
        return userRepository.existsByEmail(email);
    }

    // 닉네임 중복체크.
    public boolean checkNickName(String nickName) {
        return userRepository.existsByNickName(nickName);
    }

    public void check(UserDTO.SignUpUserDto signUpUserDto) {
    }
}
