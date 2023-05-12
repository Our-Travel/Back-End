package com.example.ot.app.member.service;

import com.example.ot.app.base.dto.RsData;
import com.example.ot.app.base.security.jwt.JwtProvider;
import com.example.ot.app.member.dto.MemberDTO;
import com.example.ot.app.member.entity.Member;
import com.example.ot.app.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    // 회원가입 생성.
    @Transactional
    public void create(MemberDTO.SignUpDto signUpDto){
        Member member = Member.builder()
                .username(signUpDto.getUsername())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .nickName(signUpDto.getNickName())
                .regionLevel1(signUpDto.getRegionLevel1())
                .regionLevel2(signUpDto.getRegionLevel2())
                .build();
        
        memberRepository.save(member);
        log.info("회원가입 완료");
    }

    // 아이디 중복체크.
    public  RsData<Member> checkUsername(String username){
        if(memberRepository.existsByUsername(username)){
            return RsData.of("F-1", "중복된 이메일입니다.");
        }
        return  RsData.of("S-1", "중복 없음.");
    }

    // 닉네임 중복체크.
    public RsData<Member> checkNickName(String nickName) {
        if(memberRepository.existsByNickName(nickName)){
            return RsData.of("F-1", "중복된 닉네임입니다.");
        }
        return  RsData.of("S-1", "중복 없음.");
    }

    // 아이디 닉네임 동시체크
    public RsData<Member> check(MemberDTO.SignUpDto signUpDto) {
        if(checkUsername(signUpDto.getUsername()).isFail());
        if(checkNickName(signUpDto.getNickName()).isFail());
        return RsData.of("S-1", "중복 없음.");
    }

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    public String genAccessToken(Member member) {
        return jwtProvider.generateAccessToken(member.getAccessTokenClaims(), 60 * 60 * 24 * 90);
    }
}