package com.example.ot.app.host.service;

import com.example.ot.app.base.rsData.RsData;
import com.example.ot.app.hashtag.service.HashTagService;
import com.example.ot.app.host.dto.RegisterHostDTO;
import com.example.ot.app.host.entity.Host;
import com.example.ot.app.host.repository.HostRepository;
import com.example.ot.app.member.entity.Member;
import com.example.ot.app.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class HostService {

    private final HostRepository hostRepository;
    private final MemberRepository memberRepository;
    private final HashTagService hashTagService;

    @Transactional
    public RsData<Host> createHost(RegisterHostDTO registerHostDTO, long id){
        Member member = memberRepository.findById(id).orElse(null);
        if(ObjectUtils.isEmpty(member)){
            return RsData.of("F-1", "로그인 후 이용해주세요.");
        }
        if(ObjectUtils.isEmpty(registerHostDTO.getRegionCode())){
            return RsData.of("F-1", "지역을 선택해주세요.");
        }
        Host host = Host
                .builder()
                .introduction(registerHostDTO.getIntroduction())
                .member(member)
                .regionCode(registerHostDTO.getRegionCode())
                .build();
        hostRepository.save(host);
        member.setHostAuthority(true);
        hashTagService.applyHashTags(host, registerHostDTO.getHashTag());
        return RsData.of("S-1", "Host 등록이 완료되었습니다.");
    }
}
