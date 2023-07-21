package com.example.ot.app.member.service;

import com.example.ot.app.member.dto.request.SignUpRequest;
import com.example.ot.app.member.dto.response.MyPageResponse;
import com.example.ot.app.member.entity.Member;
import com.example.ot.app.member.entity.ProfileImage;
import com.example.ot.app.member.exception.MemberException;
import com.example.ot.app.member.repository.MemberRepository;
import com.example.ot.app.member.repository.ProfileImageRepository;
import com.example.ot.base.s3.S3ProfileUploader;
import com.example.ot.config.AppConfig;
import com.example.ot.config.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

import static com.example.ot.app.member.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final ProfileImageRepository profileImageRepository;
    private final S3ProfileUploader profileUploader;

    @Transactional
    public void create(SignUpRequest signUpRequest){
        checkUsernameAndNickName(signUpRequest);
        create("OT", signUpRequest);
    }

    // 회원가입 생성.
    private void create(String providerTypeCode, SignUpRequest signUpRequest){
        String password = passwordEncoder.encode(signUpRequest.getPassword());
        Member member = Member.of(providerTypeCode, signUpRequest, password);
        memberRepository.save(member);
    }

    // 아이디 중복체크.
    public void checkUsername(String username) {
        if(memberRepository.existsByUsername(username)){
            throw new MemberException(USERNAME_EXISTS);
        }
    }

    // 닉네임 중복체크.
    public void checkNickName(String nickName) {
        if(memberRepository.existsByNickName(nickName)){
            throw new MemberException(NICKNAME_EXISTS);
        }
    }

    // 아이디 닉네임 동시체크
    private void checkUsernameAndNickName(SignUpRequest signUpRequest) {
        checkUsername(signUpRequest.getUsername());
        checkNickName(signUpRequest.getNickName());
    }

    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username).orElseThrow(() -> new MemberException(USERNAME_NOT_EXISTS));
    }

    public Member findByMemberId(Long id){
        return memberRepository.findById(id).orElseThrow(() -> new MemberException(MEMBER_NOT_EXISTS));
    }

    public Member verifyUsername(String username) {
        return findByUsername(username);
    }

    public void verifyPassword(String password, String inputPassword) {
        if (!passwordEncoder.matches(inputPassword, password)) {
            throw new MemberException(PASSWORD_MISMATCH);
        }
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

    public Member getByMemberId__cached(Long id) {
        MemberService thisObj = (MemberService) AppConfig.getContext().getBean("memberService");
        Map<String, Object> memberMap = thisObj.getMemberMapByMemberId__cached(id);

        return Member.fromMap(memberMap);
    }

    @Cacheable("member")
    public Map<String, Object> getMemberMapByMemberId__cached(Long id) {
        Member member = findByMemberId(id);
        return member.toMap();
    }

    @CachePut("member")
    public Map<String, Object> putMemberMapByUsername__cached(Long id) {
        Member member = findByMemberId(id);
        return member.toMap();
    }

    public MyPageResponse getMemberInfo(Long memberId) {
        Member member = findByMemberId(memberId);
        ProfileImage profileImage = getMemberProfileImage(member.getId());
        return MyPageResponse.fromMember(member, profileImage);
    }

    public ProfileImage getMemberProfileImage(Long memberId){
        return profileImageRepository.findByMemberId(memberId).orElse(null);
    }

    @Transactional
    public void updateProfile(Long id, MultipartFile file) throws IOException {
        Member member = findByMemberId(id);

        //기존에 프로필 이미지가 있으면 기존거 삭제하고 업로드
        ProfileImage findProfileImage = profileImageRepository.findByMember(member).orElse(null);

        if(!ObjectUtils.isEmpty(findProfileImage)){
            ProfileImage changeProfile = profileUploader.updateFile(findProfileImage.getStoredFileName(), file);
            findProfileImage.updateProfile(changeProfile);
        }
        else{
            ProfileImage profileImage = profileUploader.uploadFile(file);
            profileImage.setMember(member);
            profileImageRepository.save(profileImage);
        }
    }
}
