package com.example.ot.app.host.service;

import com.example.ot.app.hashtag.service.HashTagService;
import com.example.ot.app.host.dto.request.WriteHostInfoRequest;
import com.example.ot.app.host.dto.response.EditHostResponse;
import com.example.ot.app.host.dto.response.HostInfoListResponse;
import com.example.ot.app.host.entity.Host;
import com.example.ot.app.host.exception.HostException;
import com.example.ot.app.host.repository.HostRepository;
import com.example.ot.app.member.entity.Member;
import com.example.ot.app.member.entity.ProfileImage;
import com.example.ot.app.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.ot.app.host.exception.ErrorCode.HOST_NOT_EXISTS;
import static com.example.ot.app.host.exception.ErrorCode.HOST_NOT_EXISTS_BY_REGION;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HostService {

    private final HostRepository hostRepository;
    private final HashTagService hashTagService;
    private final MemberService memberService;

    @Transactional
    public void createHost(WriteHostInfoRequest writeHostInfoRequest, Long memberId){
        Member member = memberService.findByMemberId(memberId);
        Host host = Host.of(writeHostInfoRequest, member);
        hostRepository.save(host);
        member.assignHostRole();
//        memberService.putMemberMapByUsername__cached(member.getId());
        hashTagService.applyHashTags(host, writeHostInfoRequest.getHashTag());
    }

    public Host findByMemberId(Long memberId){
        return hostRepository.findByMemberId(memberId).orElseThrow(() -> new HostException(HOST_NOT_EXISTS));
    }

    public EditHostResponse getHostInfo(Long memberId) {
        Host host = findByMemberId(memberId);
        String hostHashTag = hashTagService.getHashTag(host.getId());
        return EditHostResponse.fromHost(host, hostHashTag);
    }

    @Transactional
    public void updateHostInfo(WriteHostInfoRequest writeHostInfoRequest, Long memberId) {
        Host host = findByMemberId(memberId);
        host.updateHostInfo(writeHostInfoRequest.getIntroduction(), writeHostInfoRequest.getRegionCode());
        hashTagService.updateHashTag(writeHostInfoRequest.getHashTag(), host);
    }

    @Transactional
    public void removeHostAuthorize(Long memberId) {
        Host host = findByMemberId(memberId);
        memberService.findByMemberId(memberId).removeHostRole();
        hashTagService.deleteHashTag(host.getId());
        hostRepository.delete(host);
    }

    public List<HostInfoListResponse> getHostListByRegion(Integer regionCode) {
        List<Host> hostList = hostRepository.findHostByRegionCode(regionCode);

        if (hostList.isEmpty()) {
            throw new HostException(HOST_NOT_EXISTS_BY_REGION);
        }

        return hostList.stream()
                .map(this::mapToHostInfoListResponse)
                .collect(Collectors.toList());
    }

    private HostInfoListResponse mapToHostInfoListResponse(Host host) {
        String hashTag = hashTagService.getHashTag(host.getId());
        ProfileImage hostProfileImage = memberService.getMemberProfileImage(host.getMember().getId());
        return HostInfoListResponse.fromHost(host, hashTag, hostProfileImage);
    }
}
