package com.example.ot.app.member.service;

import com.example.ot.app.member.dto.request.SignInRequest;
import com.example.ot.app.member.dto.request.SignUpRequest;
import com.example.ot.app.member.dto.request.UpdateMemberRequest;
import com.example.ot.app.member.dto.response.MyPageResponse;
import com.example.ot.app.member.entity.Member;
import com.example.ot.app.member.entity.ProfileImage;
import com.example.ot.app.member.exception.MemberException;
import com.example.ot.app.member.repository.MemberRepository;
import com.example.ot.app.member.repository.ProfileImageRepository;
import com.example.ot.base.s3.S3ProfileUploader;
import com.example.ot.config.AppConfig;
import com.example.ot.config.security.jwt.JwtUtils;
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
    private final JwtUtils jwtUtils;
    private final ProfileImageRepository profileImageRepository;
    private final S3ProfileUploader profileUploader;

    @Transactional
    public void createMember(SignUpRequest signUpRequest){
        checkUsernameAndNickName(signUpRequest);
        createMember("OT", signUpRequest);
    }

    public Member createMember(String providerTypeCode, SignUpRequest signUpRequest) {
        String password = passwordEncoder.encode(signUpRequest.getPassword());
        Member member = Member.create(providerTypeCode, signUpRequest, password);
        memberRepository.save(member);
        return member;
    }

    public void checkUsername(String username) {
        if(memberRepository.existsByUsername(username)){
            throw new MemberException(USERNAME_EXISTS);
        }
    }

    public void checkNickName(String nickName) {
        if(memberRepository.existsByNickName(nickName)){
            throw new MemberException(NICKNAME_EXISTS);
        }
    }

    @Transactional
    public String validSignInAndGetAccessToken(SignInRequest signInRequest) {
        Member member = memberRepository.findByUsername(signInRequest.getUsername())
                .orElseThrow(() -> new MemberException(USERNAME_NOT_EXISTS));
        verifyPassword(member.getPassword(), signInRequest.getPassword());
        return genAccessToken(member);
    }

    private void checkUsernameAndNickName(SignUpRequest signUpRequest) {
        checkUsername(signUpRequest.getUsername());
        checkNickName(signUpRequest.getNickName());
    }

    public Member findByMemberId(Long memberId){
        return memberRepository.findById(memberId).orElseThrow(() -> new MemberException(MEMBER_NOT_EXISTS));
    }

    public void verifyPassword(String password, String inputPassword) {
        if (!passwordEncoder.matches(inputPassword, password)) {
            throw new MemberException(PASSWORD_MISMATCH);
        }
    }

    public void verifyPassword(Long memberId, String inputPassword) {
        Member member = findByMemberId(memberId);
        verifyPassword(member.getPassword(), inputPassword);
    }

    @Transactional
    public String genAccessToken(Member member) {
        String accessToken = member.getAccessToken();

        if (!StringUtils.hasLength(accessToken)) {
            accessToken = jwtUtils.generateAccessToken(member.getAccessTokenClaims());
            member.generateAccessToken(accessToken);
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
    public Map<String, Object> getMemberMapByMemberId__cached(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_EXISTS));
        return member.toMap();
    }

    @CachePut("member")
    public Map<String, Object> putMemberMapByUsername__cached(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_EXISTS));
        return member.toMap();
    }

    public MyPageResponse getMemberInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_EXISTS));
        ProfileImage profileImage = getMemberProfileImage(memberId);
        return MyPageResponse.fromMember(member, profileImage);
    }

    public ProfileImage getMemberProfileImage(Long memberId){
        return profileImageRepository.findProfileImageByMemberId(memberId).orElse(null);
    }

    @Transactional
    public void updateProfileImage(Long memberId, MultipartFile file) throws IOException {
        ProfileImage findProfileImage = getMemberProfileImage(memberId);

        if(!ObjectUtils.isEmpty(findProfileImage)){
            ProfileImage changeProfile = profileUploader.updateFile(findProfileImage.getStoredFileName(), file);
            findProfileImage.updateProfile(changeProfile);
        }
        else{
            ProfileImage profileImage = profileUploader.uploadFile(file);
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new MemberException(MEMBER_NOT_EXISTS));
            profileImage.setMember(member);
            profileImageRepository.save(profileImage);
        }
    }

    @Transactional
    public void deleteProfileImage(Long memberId) {
        ProfileImage profileImage = getMemberProfileImage(memberId);
        if(!ObjectUtils.isEmpty(profileImage)){
            profileUploader.deleteS3(profileImage.getStoredFileName());
            profileImageRepository.delete(profileImage);
        }
    }

    @Transactional
    public void updatePassword(UpdateMemberRequest updateMemberRequest, Long memberId) {
        String newPassword = updateMemberRequest.getPassword();
        String verifyPassword = updateMemberRequest.getVerifyPassword();

        verifyPasswordsMatch(newPassword, verifyPassword);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_EXISTS));
        String encodedPassword = passwordEncoder.encode(newPassword);
        member.updatePassword(encodedPassword);
    }

    private void verifyPasswordsMatch(String password, String verifyPassword) {
        if (!password.equals(verifyPassword)) {
            throw new MemberException(PASSWORD_MISMATCH);
        }
    }

    @Transactional
    public void updateNickName(String nickName, Long memberId) {
        Member member = findByMemberId(memberId);
        member.updateNickName(nickName);
    }

}
