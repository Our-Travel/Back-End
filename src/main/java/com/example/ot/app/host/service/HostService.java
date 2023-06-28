package com.example.ot.app.host.service;

import com.example.ot.app.base.rsData.RsData;
import com.example.ot.app.hashtag.service.HashTagService;
import com.example.ot.app.host.dto.request.RegisterHostRequest;
import com.example.ot.app.host.entity.Host;
import com.example.ot.app.host.exception.HostException;
import com.example.ot.app.host.repository.HostRepository;
import com.example.ot.app.member.entity.Member;
import com.example.ot.app.member.exception.MemberException;
import com.example.ot.app.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import static com.example.ot.app.host.exception.ErrorCode.*;
import static com.example.ot.app.member.exception.ErrorCode.MEMBER_NOT_EXISTS;

@Service
@RequiredArgsConstructor
public class HostService {

    private final HostRepository hostRepository;
    private final MemberRepository memberRepository;
    private final HashTagService hashTagService;

    @Transactional
    public void createHost(RegisterHostRequest registerHostRequest, long id){
        Member member = memberRepository.findById(id).orElseThrow(() -> new MemberException(MEMBER_NOT_EXISTS));
        if(ObjectUtils.isEmpty(registerHostRequest.getRegionCode())){
            throw new HostException(NO_REGION_CODE);
        }
        Host host = Host
                .builder()
                .introduction(registerHostRequest.getIntroduction())
                .member(member)
                .regionCode(registerHostRequest.getRegionCode())
                .build();
        hostRepository.save(host);
        member.setHostAuthority(true);
        hashTagService.applyHashTags(host, registerHostRequest.getHashTag());
    }
}
