package com.example.ot.app.host.service;

import com.example.ot.app.hashtag.service.HashTagService;
import com.example.ot.app.host.dto.request.WriteHostInfoRequest;
import com.example.ot.app.host.dto.response.EditHostResponse;
import com.example.ot.app.host.entity.Host;
import com.example.ot.app.host.exception.HostException;
import com.example.ot.app.host.repository.HostRepository;
import com.example.ot.app.member.entity.Member;
import com.example.ot.app.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.ot.app.host.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HostService {

    private final HostRepository hostRepository;
    private final HashTagService hashTagService;
    private final MemberService memberService;

    @Transactional
    public void createHost(WriteHostInfoRequest writeHostInfoRequest, Long id){
        Member member = memberService.findById(id);
        Host host = Host
                .builder()
                .introduction(writeHostInfoRequest.getIntroduction())
                .member(member)
                .regionCode(writeHostInfoRequest.getRegionCode())
                .build();
        hostRepository.save(host);
        member.setHostAuthority(true);
//        memberService.putMemberMapByUsername__cached(member.getId());
        hashTagService.applyHashTags(host, writeHostInfoRequest.getHashTag());
    }

    public EditHostResponse getHostInfo(Long id) {
        Host host = hostRepository.findByMemberId(id).orElseThrow(() -> new HostException(HOST_NOT_EXISTS));
        String hostHashTag = hashTagService.getHashTag(host.getId());
        return new EditHostResponse(host.getIntroduction(), hostHashTag, host.getRegionCode());
    }

    @Transactional
    public void updateHostInfo(WriteHostInfoRequest writeHostInfoRequest, Long id) {
        Host host = hostRepository.findByMemberId(id).orElseThrow(() -> new HostException(HOST_NOT_EXISTS));
        host.updateHostInfo(writeHostInfoRequest.getIntroduction(), writeHostInfoRequest.getRegionCode());
        hashTagService.updateHashTags(writeHostInfoRequest.getHashTag(), host);
    }

    @Transactional
    public void removeHostAuthorize(Long id) {
        Host host = hostRepository.findByMemberId(id).orElseThrow(() -> new HostException(HOST_NOT_EXISTS));
        memberService.findById(id).setHostAuthority(false);
        hashTagService.deleteHashTag(host.getId());
        hostRepository.delete(host);
    }
}
