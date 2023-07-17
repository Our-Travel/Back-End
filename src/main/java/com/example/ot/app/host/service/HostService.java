package com.example.ot.app.host.service;

import com.example.ot.app.hashtag.service.HashTagService;
import com.example.ot.app.host.dto.request.RegisterHostRequest;
import com.example.ot.app.host.dto.response.EditHostResponse;
import com.example.ot.app.host.entity.Host;
import com.example.ot.app.host.exception.HostException;
import com.example.ot.app.host.repository.HostRepository;
import com.example.ot.app.member.entity.Member;
import com.example.ot.app.member.exception.MemberException;
import com.example.ot.app.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import static com.example.ot.app.host.exception.ErrorCode.*;
import static com.example.ot.app.member.exception.ErrorCode.NOT_EXISTS_USERNAME;

@Service
@RequiredArgsConstructor
public class HostService {

    private final HostRepository hostRepository;
    private final HashTagService hashTagService;
    private final MemberService memberService;

    @Transactional
    public void createHost(RegisterHostRequest registerHostRequest, long id){
        Member member = memberService.findById(id);
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
//        memberService.putMemberMapByUsername__cached(member.getId());
        hashTagService.applyHashTags(host, registerHostRequest.getHashTag());
    }

    public EditHostResponse getMemberHostInfo(Long id) {
        Host host = hostRepository.findByMemberId(id).orElseThrow(() -> new MemberException(NOT_EXISTS_USERNAME));
        String hostHashTag = hashTagService.getHashTag(host.getId());
        return new EditHostResponse(host.getIntroduction(), hostHashTag, host.getRegionCode());
    }
}
