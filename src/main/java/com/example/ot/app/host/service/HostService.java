package com.example.ot.app.host.service;

import com.example.ot.app.hashtag.entity.HashTag;
import com.example.ot.app.hashtag.repository.HashTagRepository;
import com.example.ot.app.host.dto.request.WriteHostInfoRequest;
import com.example.ot.app.host.dto.response.EditHostResponse;
import com.example.ot.app.host.dto.response.HostCountResponse;
import com.example.ot.app.host.dto.response.HostInfoListResponse;
import com.example.ot.app.host.entity.Host;
import com.example.ot.app.host.exception.HostException;
import com.example.ot.app.host.repository.HostRepository;
import com.example.ot.app.keyword.entity.Keyword;
import com.example.ot.app.keyword.repository.KeywordRepository;
import com.example.ot.app.member.entity.Member;
import com.example.ot.app.member.entity.ProfileImage;
import com.example.ot.app.member.repository.MemberRepository;
import com.example.ot.app.member.repository.ProfileImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.ot.app.host.exception.ErrorCode.HOST_NOT_EXISTS;
import static com.example.ot.app.host.exception.ErrorCode.HOST_NOT_EXISTS_BY_REGION;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HostService {

    private final HostRepository hostRepository;
    private final KeywordRepository keywordRepository;
    private final HashTagRepository hashTagRepository;
    private final MemberRepository memberRepository;
    private final ProfileImageRepository profileImageRepository;

    @Transactional
    public void createHost(WriteHostInfoRequest writeHostInfoRequest, Long memberId){
        Member member = memberRepository.findByMemberId(memberId);
        Host host = Host.of(writeHostInfoRequest, member);
        hostRepository.save(host);
        member.assignHostRole();
//        memberService.putMemberMapByUsername__cached(member.getId());
        applyHostHashTags(host, writeHostInfoRequest.getHashTag());
    }

    public EditHostResponse getHostInfo(Long memberId) {
        Host host = hostRepository.findHostByMember_Id(memberId)
                .orElseThrow(() -> new HostException(HOST_NOT_EXISTS));;
        String hostHashTag = getHostHashTag(host.getId());
        return EditHostResponse.fromHost(host, hostHashTag);
    }

    @Transactional
    public void updateHostInfo(WriteHostInfoRequest writeHostInfoRequest, Long memberId) {
        Host host = hostRepository.findHostByMember_Id(memberId)
                .orElseThrow(() -> new HostException(HOST_NOT_EXISTS));;
        host.updateHostInfo(writeHostInfoRequest.getIntroduction(), writeHostInfoRequest.getRegionCode());
        updateHashTag(writeHostInfoRequest.getHashTag(), host);
    }

    @Transactional
    public void removeHostAuthorize(Long memberId) {
        Host host = hostRepository.findHostByMember_Id(memberId)
                .orElseThrow(() -> new HostException(HOST_NOT_EXISTS));;
        memberRepository.findByMemberId(memberId).removeHostRole();
        deleteHashTag(host.getId());
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
        String hashTag = getHostHashTag(host.getId());
        ProfileImage hostProfileImage = profileImageRepository.findProfileImageByMemberId(host.getMemberId())
                .orElse(null);
        return HostInfoListResponse.fromHost(host, hashTag, hostProfileImage);
    }

    private String getHostHashTag(Long hostId){
        List<HashTag> hashTags = hashTagRepository.findByHostId(hostId);
        return hashTags.stream()
                .map(hashTag -> "#" + hashTag.getKeyword().getContent())
                .collect(Collectors.joining());
    }

    public void applyHostHashTags(Host host, String keywordContentsStr) {
        List<String> keywordContents = Arrays.stream(keywordContentsStr.split("#"))
                .map(String::trim)
                .filter(s -> s.length() > 0).toList();

        keywordContents.forEach(keywordContent -> {
            saveHashTag(host, keywordContent);
        });
    }

    private void saveHashTag(Host host, String keywordContent) {
        Keyword keyword = saveKeyword(keywordContent);
        Optional<HashTag> opHashTag = hashTagRepository.findByHostIdAndKeywordId(host.getId(), keyword.getId());
        if (opHashTag.isPresent()) {
            return;
        }
        HashTag hashTag = HashTag.of(host, keyword);
        hashTagRepository.save(hashTag);
    }

    public Keyword saveKeyword(String keywordContent) {
        Optional<Keyword> optKeyword = keywordRepository.findByContent(keywordContent);
        if ( optKeyword.isPresent() ) {
            return optKeyword.get();
        }
        Keyword keyword = Keyword.of(keywordContent);
        keywordRepository.save(keyword);
        return keyword;
    }

    private void updateHashTag(String hashTag, Host host){
        deleteHashTag(host.getId());
        applyHostHashTags(host, hashTag);
    }

    private void deleteHashTag(Long hostId) {
        List<HashTag> hostHashTags = hashTagRepository.findByHostId(hostId);
        hashTagRepository.deleteAll(hostHashTags);
    }

    public List<HostCountResponse> getHostCountByRegion() {
        List<HostCountResponse> hostCountResponseList = hostRepository.countHostsByRegionCode();
        return hostCountResponseList;
    }
}
