package com.example.ot.app.member.service;

import com.example.ot.app.base.rsData.RsData;
import com.example.ot.app.member.dto.MemberDTO;
import com.example.ot.app.member.entity.Member;
import com.example.ot.app.member.repository.MemberRepository;
import com.example.ot.config.AppConfig;
import com.example.ot.config.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional
    public void create(MemberDTO.SignUpDto signUpDto){
        create("OT", signUpDto);
    }

    // 회원가입 생성.
    private void create(String providerTypeCode, MemberDTO.SignUpDto signUpDto){
        Member member = Member.builder()
                .username(signUpDto.getUsername())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .nickName(signUpDto.getNickName())
                .providerTypeCode(providerTypeCode)
                .build();
        
        memberRepository.save(member);
        log.info("회원가입 완료");
    }

    // 아이디 중복체크.
    public RsData<Member> checkUsername(String username) {
        return constructResponse(memberRepository.existsByUsername(username), "이메일");
    }

    // 닉네임 중복체크.
    public RsData<Member> checkNickName(String nickName) {
        return constructResponse(memberRepository.existsByNickName(nickName), "닉네임");
    }

    // 아이디 닉네임 동시체크
    public RsData<Member> check(MemberDTO.SignUpDto signUpDto) {
        RsData<Member> usernameCheckResult = checkUsername(signUpDto.getUsername());
        if (isDuplicate(usernameCheckResult)) {
            return usernameCheckResult;
        }

        RsData<Member> nickNameCheckResult = checkNickName(signUpDto.getNickName());
        if (isDuplicate(nickNameCheckResult)) {
            return nickNameCheckResult;
        }

        return RsData.of("S-1", "중복 없음.");
    }

    // 응답 메세지 구성
    private RsData<Member> constructResponse(boolean exists, String field) {
        return exists ? RsData.of("F-1", "중복된 " + field + "입니다.") : RsData.of("S-1", "중복 없음.");
    }

    // 중복 체크 결과 확인
    private boolean isDuplicate(RsData<Member> rsData) {
        return "F-1".equals(rsData.getResultCode());
    }

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }
    public Optional<Member> findById(Long id){
        return memberRepository.findById(id);
    }

    @Transactional
    public String genAccessToken(Member member) {
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
        Member member = findById(id).orElse(null);
        return member.toMap();
    }
}
