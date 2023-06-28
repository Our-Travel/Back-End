package com.example.ot.app.member.service;

import com.example.ot.app.member.dto.request.SignInRequest;
import com.example.ot.app.member.dto.request.SignUpRequest;
import com.example.ot.app.member.entity.Member;
import com.example.ot.app.member.exception.MemberException;
import com.example.ot.app.member.repository.MemberRepository;
import com.example.ot.config.AppConfig;
import com.example.ot.config.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Map;

import static com.example.ot.app.member.exception.ErrorCode.*;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional
    public void create(SignUpRequest signUpRequest){
        create("OT", signUpRequest);
    }

    // 회원가입 생성.
    private void create(String providerTypeCode, SignUpRequest signUpRequest){
        Member member = Member.builder()
                .username(signUpRequest.getUsername())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .nickName(signUpRequest.getNickName())
                .providerTypeCode(providerTypeCode)
                .build();
        
        memberRepository.save(member);
        log.info("회원가입 완료");
    }

    // 아이디 중복체크.
    public void checkUsername(String username) {
        if(!ObjectUtils.isEmpty(findByUsername(username))){
            throw new MemberException(EXISTS_USERNAME);
        }
    }

    // 닉네임 중복체크.
    public void checkNickName(String nickName) {
        if(memberRepository.existsByNickName(nickName)){
            throw new MemberException(EXISTS_NICKNAME);
        }
    }

    // 아이디 닉네임 동시체크
    public void check(SignUpRequest signUpRequest) {
        checkUsername(signUpRequest.getUsername());
        checkNickName(signUpRequest.getNickName());
    }

    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username).orElseThrow(() -> new MemberException(NOT_EXISTS_USERNAME));
    }

    public Member findById(Long id){
        return memberRepository.findById(id).orElseThrow(() -> new MemberException(MEMBER_NOT_EXISTS));
    }

    public void verifyUsername(String username) {
        findByUsername(username);
    }

    public void verifyPassword(SignInRequest signInRequest) {
        Member member = findByUsername(signInRequest.getUsername());
        if (!passwordEncoder.matches(signInRequest.getPassword(), member.getPassword())) {
            throw new MemberException(WRONG_PASSWORD);
        }
    }

    @Transactional
    public String genAccessToken(String username) {
        Member member = findByUsername(username);
        String accessToken = member.getAccessToken();

        if (!StringUtils.hasLength(accessToken)) {
            accessToken = jwtProvider.generateAccessToken(member.getAccessTokenClaims(), 60L * 60 * 24 * 365 * 100);
            member.setAccessToken(accessToken);
        }

        return accessToken;
    }

    public boolean verifyWithWhiteList(Member member, String token) {
        return member.getAccessToken().equals(token);
    }

    public Member getByMemberId__cached(long id) {
        MemberService thisObj = (MemberService) AppConfig.getContext().getBean("memberService");
        Map<String, Object> memberMap = thisObj.getMemberMapByMemberId__cached(id);

        return Member.fromMap(memberMap);
    }

    @Cacheable("member")
    public Map<String, Object> getMemberMapByMemberId__cached(long id) {
        Member member = findById(id);
        return member.toMap();
    }
}
