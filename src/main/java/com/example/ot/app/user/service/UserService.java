package com.example.ot.app.user.service;

import com.example.ot.app.base.dto.RsData;
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
    public  RsData<User> checkEmail(String email){
        if(userRepository.existsByEmail(email)){
            return RsData.of("F-1", "중복된 이메일입니다.");
        }
        return  RsData.of("S-1", "중복 없음.");
    }

    // 닉네임 중복체크.
    public RsData<User> checkNickName(String nickName) {
        if(userRepository.existsByNickName(nickName)){
            return RsData.of("F-1", "중복된 닉네임입니다.");
        }
        return  RsData.of("S-1", "중복 없음.");
    }

    // 닉네임 이메일 동시체크
    public RsData<User> check(UserDTO.SignUpUserDto signUpUserDto) {
        if(checkEmail(signUpUserDto.getEmail()).isFail());
        if(checkNickName(signUpUserDto.getNickName()).isFail());
        return RsData.of("S-1", "중복 없음.");
    }
}
